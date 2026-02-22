import { useEffect, useMemo, useState } from "react";
import {
  createAdminRoute,
  createAdminRouteStop,
  createAdminStop,
  deleteAdminRoute,
  deleteAdminRouteStop,
  deleteAdminStop,
  fetchAdminBookings,
  fetchAdminRouteStops,
  fetchAdminUsers,
  fetchRoutes,
  fetchStops,
  updateAdminBookingTravelStatus
} from "../api";
import { clearAuthSession } from "../auth";
import { useNavigate } from "react-router-dom";
import { UserIcon } from "../components/Icons";

export default function AdminDashboardPage() {
  const navigate = useNavigate();
  const [stops, setStops] = useState([]);
  const [routes, setRoutes] = useState([]);
  const [routeStops, setRouteStops] = useState([]);
  const [users, setUsers] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [stopName, setStopName] = useState("");
  const [routeColor, setRouteColor] = useState("");
  const [mappingRouteId, setMappingRouteId] = useState("");
  const [mappingStopId, setMappingStopId] = useState("");
  const [mappingOrder, setMappingOrder] = useState(1);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");

  const routePathGroups = useMemo(() => {
    const grouped = {};
    for (const mapping of routeStops) {
      const key = String(mapping.routeId);
      if (!grouped[key]) {
        grouped[key] = {
          routeId: mapping.routeId,
          routeColor: mapping.routeColor,
          items: []
        };
      }
      grouped[key].items.push(mapping);
    }

    return Object.values(grouped)
      .map((group) => ({
        ...group,
        items: [...group.items].sort((a, b) => Number(a.stopOrder) - Number(b.stopOrder))
      }))
      .sort((a, b) => String(a.routeColor).localeCompare(String(b.routeColor)));
  }, [routeStops]);

  async function loadData() {
    try {
      setLoading(true);
      setError("");
      const [stopsData, routesData, routeStopsData, usersData, bookingsData] = await Promise.all([
        fetchStops(),
        fetchRoutes(),
        fetchAdminRouteStops(),
        fetchAdminUsers(),
        fetchAdminBookings()
      ]);
      setStops(stopsData || []);
      setRoutes(routesData || []);
      setRouteStops(routeStopsData || []);
      setUsers(usersData || []);
      setBookings(bookingsData || []);
    } catch (err) {
      if ((err.message || "").toLowerCase().includes("access")) {
        clearAuthSession();
        navigate("/login", { replace: true });
        return;
      }
      setError(err.message || "Failed to load admin data");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadData();
  }, []);

  async function onCreateStop(event) {
    event.preventDefault();
    try {
      setBusy(true);
      await createAdminStop({ name: stopName });
      setStopName("");
      await loadData();
    } catch (err) {
      setError(err.message || "Failed to create stop");
    } finally {
      setBusy(false);
    }
  }

  async function onCreateRoute(event) {
    event.preventDefault();
    try {
      setBusy(true);
      await createAdminRoute({ color: routeColor });
      setRouteColor("");
      await loadData();
    } catch (err) {
      setError(err.message || "Failed to create route");
    } finally {
      setBusy(false);
    }
  }

  async function onCreateMapping(event) {
    event.preventDefault();
    try {
      setBusy(true);
      await createAdminRouteStop({
        routeId: Number(mappingRouteId),
        stopId: Number(mappingStopId),
        stopOrder: Number(mappingOrder)
      });
      setMappingOrder(1);
      await loadData();
    } catch (err) {
      setError(err.message || "Failed to create route-stop mapping");
    } finally {
      setBusy(false);
    }
  }

  async function onDeleteStop(id) {
    try {
      setBusy(true);
      await deleteAdminStop(id);
      await loadData();
    } catch (err) {
      setError(err.message || "Failed to delete stop");
    } finally {
      setBusy(false);
    }
  }

  async function onDeleteRoute(id) {
    try {
      setBusy(true);
      await deleteAdminRoute(id);
      await loadData();
    } catch (err) {
      setError(err.message || "Failed to delete route");
    } finally {
      setBusy(false);
    }
  }

  async function onDeleteMapping(id) {
    try {
      setBusy(true);
      await deleteAdminRouteStop(id);
      await loadData();
    } catch (err) {
      setError(err.message || "Failed to delete mapping");
    } finally {
      setBusy(false);
    }
  }

  async function onToggleTravel(bookingId, travelled) {
    try {
      setBusy(true);
      await updateAdminBookingTravelStatus(bookingId, travelled);
      await loadData();
    } catch (err) {
      setError(err.message || "Failed to update travel status");
    } finally {
      setBusy(false);
    }
  }

  function handleLogout() {
    clearAuthSession();
    navigate("/login", { replace: true });
  }

  return (
    <div className="admin-page">
      <section className="hero compact">
        <p className="hero-pill">Admin Portal</p>
        <h1>Metro Data And User Monitoring</h1>
        <p className="hero-subtitle">
          Manage metro data, inspect users, and track travelled/not-travelled bookings.
        </p>
        <div className="hero-actions">
          <button className="primary-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </section>

      {loading && <p className="hint">Loading admin data...</p>}
      {error && <p className="error">{error}</p>}

      <section className="panel admin-grid">
        <article>
          <h2>Add Stop</h2>
          <form className="inline-form" onSubmit={onCreateStop}>
            <input
              value={stopName}
              onChange={(event) => setStopName(event.target.value)}
              placeholder="Stop name"
              required
            />
            <button className="primary-btn" disabled={busy}>
              Add Stop
            </button>
          </form>
          <div className="list-block">
            {stops.map((stop) => (
              <div className="list-row" key={stop.id}>
                <span>{stop.name}</span>
                <button className="danger-btn" onClick={() => onDeleteStop(stop.id)}>
                  Delete
                </button>
              </div>
            ))}
          </div>
        </article>

        <article>
          <h2>Add Route</h2>
          <form className="inline-form" onSubmit={onCreateRoute}>
            <input
              value={routeColor}
              onChange={(event) => setRouteColor(event.target.value)}
              placeholder="Route color"
              required
            />
            <button className="primary-btn" disabled={busy}>
              Add Route
            </button>
          </form>
          <div className="list-block">
            {routes.map((route) => (
              <div className="list-row" key={route.id}>
                <span>{route.color}</span>
                <button className="danger-btn" onClick={() => onDeleteRoute(route.id)}>
                  Delete
                </button>
              </div>
            ))}
          </div>
        </article>
      </section>

      <section className="panel">
        <h2>Route Path Mapping</h2>
        <p className="hint">
          Pick line, stop, and order number. Order 1 means first stop in that line.
        </p>

        <form className="mapping-form mapping-form-structured" onSubmit={onCreateMapping}>
          <label>
            Line
            <select
              value={mappingRouteId}
              onChange={(event) => setMappingRouteId(event.target.value)}
              required
            >
              <option value="">Select route</option>
              {routes.map((route) => (
                <option key={route.id} value={route.id}>
                  {route.color}
                </option>
              ))}
            </select>
          </label>
          <label>
            Stop
            <select
              value={mappingStopId}
              onChange={(event) => setMappingStopId(event.target.value)}
              required
            >
              <option value="">Select stop</option>
              {stops.map((stop) => (
                <option key={stop.id} value={stop.id}>
                  {stop.name}
                </option>
              ))}
            </select>
          </label>
          <label>
            Order
            <input
              type="number"
              min="1"
              value={mappingOrder}
              onChange={(event) => setMappingOrder(event.target.value)}
              required
            />
          </label>
          <button className="primary-btn" disabled={busy}>
            Add Mapping
          </button>
        </form>

        <div className="route-structure-grid">
          {routePathGroups.map((group) => (
            <article className="route-structure-card" key={group.routeId}>
              <h3>{group.routeColor} Line</h3>
              <div className="route-ordered-list">
                {group.items.map((mapping) => (
                  <div className="route-step-row" key={mapping.id}>
                    <span className="order-badge">{mapping.stopOrder}</span>
                    <span className="route-stop-name">{mapping.stopName}</span>
                    <button className="danger-btn" onClick={() => onDeleteMapping(mapping.id)}>
                      Delete
                    </button>
                  </div>
                ))}
              </div>
            </article>
          ))}
          {routePathGroups.length === 0 && <p className="hint">No route mapping found.</p>}
        </div>
      </section>

      <section className="panel">
        <h2>Registered Users</h2>
        <div className="table-head users-head">
          <span>User</span>
          <span>Email</span>
          <span>Role</span>
        </div>
        <div className="list-block structured-list">
          {users.map((user) => (
            <div className="table-row users-row" key={user.id}>
              <span>
                <UserIcon className="icon icon-xs" /> {user.username}
              </span>
              <span>{user.email}</span>
              <span className="status-chip pending">{user.role}</span>
            </div>
          ))}
          {!loading && users.length === 0 && <p className="hint">No users found.</p>}
        </div>
      </section>

      <section className="panel">
        <h2>User Bookings And Travel Status</h2>
        <div className="table-head bookings-head">
          <span>Booking</span>
          <span>User</span>
          <span>Status</span>
          <span>Actions</span>
        </div>
        <div className="list-block structured-list">
          {bookings.map((item) => (
            <article className="table-row bookings-row" key={item.bookingId}>
              <span>
                ID {item.bookingId} | {item.source?.name || item.sourceName} {"->"}{" "}
                {item.destination?.name || item.destinationName}
              </span>
              <span>
                <UserIcon className="icon icon-xs" /> {item.profile?.username || item.username}
              </span>
              <span
                className={(item.ticket?.travelled ?? item.travelled) ? "status-chip done" : "status-chip pending"}
              >
                {(item.ticket?.travelled ?? item.travelled) ? "Travelled" : "Not Travelled"}
              </span>
              <div className="history-actions">
                <button
                  className="primary-btn"
                  disabled={busy}
                  onClick={() => onToggleTravel(item.bookingId, true)}
                >
                  Travelled
                </button>
                <button
                  className="danger-btn"
                  disabled={busy}
                  onClick={() => onToggleTravel(item.bookingId, false)}
                >
                  Not Travelled
                </button>
              </div>
            </article>
          ))}
          {!loading && bookings.length === 0 && (
            <p className="hint">No bookings found.</p>
          )}
        </div>
      </section>
    </div>
  );
}
