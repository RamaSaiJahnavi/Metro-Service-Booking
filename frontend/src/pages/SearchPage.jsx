import { useEffect, useMemo, useState } from "react";
import { searchEverything } from "../api";
import { getAuthRole } from "../auth";
import { SearchIcon, UserIcon } from "../components/Icons";

function Section({ title, items, renderItem }) {
  return (
    <section className="panel search-section">
      <div className="section-title-row">
        <h2>{title}</h2>
        <span className="count-chip">{items?.length || 0}</span>
      </div>
      <div className="list-block">
        {items?.length ? items.map(renderItem) : <p className="hint">No results found in this category.</p>}
      </div>
    </section>
  );
}

export default function SearchPage() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const role = getAuthRole();
  const admin = role === "ADMIN";
  const groupedRouteMappings = useMemo(() => {
    const mappings = results?.routeStops || [];
    const grouped = {};

    for (const item of mappings) {
      const key = `${item.routeId}-${item.routeColor}`;
      if (!grouped[key]) {
        grouped[key] = {
          routeId: item.routeId,
          routeColor: item.routeColor,
          stops: []
        };
      }
      grouped[key].stops.push(item);
    }

    return Object.values(grouped)
      .map((group) => ({
        ...group,
        stops: [...group.stops].sort((a, b) => Number(a.stopOrder) - Number(b.stopOrder))
      }))
      .sort((a, b) => String(a.routeColor).localeCompare(String(b.routeColor)));
  }, [results]);

  useEffect(() => {
    runSearch("");
  }, []);

  async function runSearch(value) {
    try {
      setLoading(true);
      setError("");
      const data = await searchEverything(value);
      setResults(data);
    } catch (err) {
      setError(err.message || "Search failed");
    } finally {
      setLoading(false);
    }
  }

  function onSubmit(event) {
    event.preventDefault();
    runSearch(query);
  }

  return (
    <div className="search-page">
      <section className="hero compact search-hero">
        <p className="hero-pill">Search</p>
        <h1>Metro Data Search</h1>
        <p className="hero-subtitle">
          Find bookings, routes, and stops quickly{admin ? ", including users and route mappings" : ""}.
        </p>
        <div className="hero-badges">
          <span className="hero-badge">
            <SearchIcon className="icon icon-xs" />
            Unified Search
          </span>
        </div>
      </section>

      <section className="panel search-toolbar">
        <form className="inline-form one-line" onSubmit={onSubmit}>
          <input
            value={query}
            onChange={(event) => setQuery(event.target.value)}
            placeholder="Search by booking ID, username, route, or stop..."
          />
          <button className="primary-btn">
            <SearchIcon className="icon icon-xs" />
            Search
          </button>
        </form>
        {loading && <p className="hint">Searching...</p>}
        {error && <p className="error">{error}</p>}
      </section>

      {results && (
        <>
          <section className="search-stats">
            <article className="stat-card">
              <p>Bookings</p>
              <strong>{results.bookings?.length || 0}</strong>
            </article>
            <article className="stat-card">
              <p>Stops</p>
              <strong>{results.stops?.length || 0}</strong>
            </article>
            <article className="stat-card">
              <p>Routes</p>
              <strong>{results.routes?.length || 0}</strong>
            </article>
            {admin && (
              <>
                <article className="stat-card">
                  <p>Users</p>
                  <strong>{results.users?.length || 0}</strong>
                </article>
                <article className="stat-card">
                  <p>Mappings</p>
                  <strong>{results.routeStops?.length || 0}</strong>
                </article>
              </>
            )}
          </section>

          <Section
            title="Bookings"
            items={results.bookings || []}
            renderItem={(item) => (
              <div className="list-row" key={item.bookingId}>
                <span>
                  ID {item.bookingId} | <UserIcon className="icon icon-xs" />{" "}
                  {item.profile?.username || item.username} |{" "}
                  {item.source?.name || item.sourceName} {"->"}{" "}
                  {item.destination?.name || item.destinationName}
                </span>
                <span
                  className={(item.ticket?.travelled ?? item.travelled) ? "status-chip done" : "status-chip pending"}
                >
                  {(item.ticket?.travelled ?? item.travelled) ? "Travelled" : "Not Travelled"}
                </span>
              </div>
            )}
          />

          <Section
            title="Stops"
            items={results.stops || []}
            renderItem={(item) => (
              <div className="list-row" key={`s-${item.id}`}>
                <span>
                  {item.name}
                </span>
              </div>
            )}
          />

          <Section
            title="Routes"
            items={results.routes || []}
            renderItem={(item) => (
              <div className="list-row" key={`r-${item.id}`}>
                <span>
                  {item.color}
                </span>
              </div>
            )}
          />

          {admin && (
            <section className="panel search-section">
              <div className="section-title-row">
                <h2>Users</h2>
                <span className="count-chip">{results.users?.length || 0}</span>
              </div>
              <div className="table-head search-users-head">
                <span>User</span>
                <span>Email</span>
                <span>Role</span>
              </div>
              <div className="list-block structured-list">
                {(results.users || []).map((item) => (
                  <div className="table-row search-users-row" key={`u-${item.id}`}>
                    <span>{item.username}</span>
                    <span>{item.email}</span>
                    <span className="status-chip pending">{item.role}</span>
                  </div>
                ))}
                {(results.users || []).length === 0 && (
                  <p className="hint">No results found in this category.</p>
                )}
              </div>
            </section>
          )}

          {admin && (
            <section className="panel search-section">
              <div className="section-title-row">
                <h2>Route Mappings</h2>
                <span className="count-chip">{results.routeStops?.length || 0}</span>
              </div>
              <div className="route-structure-grid">
                {groupedRouteMappings.map((group) => (
                  <article className="route-structure-card" key={`${group.routeId}-${group.routeColor}`}>
                    <h3>{group.routeColor} Line</h3>
                    <div className="route-ordered-list">
                      {group.stops.map((item) => (
                        <div className="route-step-row" key={`m-${item.id}`}>
                          <span className="order-badge">{item.stopOrder}</span>
                          <span className="route-stop-name">{item.stopName}</span>
                        </div>
                      ))}
                    </div>
                  </article>
                ))}
                {groupedRouteMappings.length === 0 && (
                  <p className="hint">No results found in this category.</p>
                )}
              </div>
            </section>
          )}
        </>
      )}
    </div>
  );
}
