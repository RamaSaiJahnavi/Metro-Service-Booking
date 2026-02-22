import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  createBooking,
  fetchMyBookings,
  fetchStops,
  updateMyBookingTravelStatus
} from "../api";
import { clearAuthSession } from "../auth";
import BookingForm from "../components/BookingForm";
import BookingResult from "../components/BookingResult";
import { UserIcon } from "../components/Icons";

export default function BookingPage() {
  const navigate = useNavigate();
  const [stops, setStops] = useState([]);
  const [sourceId, setSourceId] = useState("");
  const [destinationId, setDestinationId] = useState("");
  const [bookingResult, setBookingResult] = useState(null);
  const [bookings, setBookings] = useState([]);
  const [searchText, setSearchText] = useState("");
  const [loadingStops, setLoadingStops] = useState(true);
  const [loadingBookings, setLoadingBookings] = useState(true);
  const [booking, setBooking] = useState(false);
  const [busyBookingId, setBusyBookingId] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    async function bootstrap() {
      await Promise.all([loadStops(), loadMyBookings()]);
    }
    bootstrap();
  }, []);

  async function loadStops() {
    try {
      setLoadingStops(true);
      const data = await fetchStops();
      setStops(Array.isArray(data) ? data : []);
    } catch (err) {
      handleAuthOrSetError(err, "Unable to load stops");
    } finally {
      setLoadingStops(false);
    }
  }

  async function loadMyBookings() {
    try {
      setLoadingBookings(true);
      const data = await fetchMyBookings();
      setBookings(Array.isArray(data) ? data : []);
    } catch (err) {
      handleAuthOrSetError(err, "Unable to load booking history");
    } finally {
      setLoadingBookings(false);
    }
  }

  function handleAuthOrSetError(err, fallbackMessage) {
    if ((err.message || "").toLowerCase().includes("access")) {
      clearAuthSession();
      navigate("/login", { replace: true });
      return;
    }
    setError(err.message || fallbackMessage);
  }

  const canBook = useMemo(() => {
    return sourceId && destinationId && sourceId !== destinationId && !booking;
  }, [sourceId, destinationId, booking]);

  const filteredBookings = useMemo(() => {
    const q = searchText.trim().toLowerCase();
    if (!q) {
      return bookings;
    }
    return bookings.filter((item) =>
      [
        item.bookingId,
        item.source?.name || item.sourceName,
        item.destination?.name || item.destinationName,
        item.ticket?.qrString || item.qrString
      ]
        .join(" ")
        .toLowerCase()
        .includes(q)
    );
  }, [bookings, searchText]);

  async function handleBooking(event) {
    event.preventDefault();
    setError("");
    setBookingResult(null);

    if (sourceId === destinationId) {
      setError("Source and destination must be different.");
      return;
    }

    try {
      setBooking(true);
      const data = await createBooking({
        sourceId: Number(sourceId),
        destinationId: Number(destinationId)
      });
      setBookingResult(data);
      await loadMyBookings();
    } catch (err) {
      handleAuthOrSetError(err, "Failed to create booking");
    } finally {
      setBooking(false);
    }
  }

  async function markTravelStatus(bookingId, travelled) {
    try {
      setBusyBookingId(bookingId);
      await updateMyBookingTravelStatus(bookingId, travelled);
      await loadMyBookings();
    } catch (err) {
      handleAuthOrSetError(err, "Failed to update travel status");
    } finally {
      setBusyBookingId(null);
    }
  }

  return (
    <div className="user-page">
      <section className="hero compact user-hero">
        <p className="hero-pill">User Portal</p>
        <h1>Book Your Metro Ride</h1>
        <p className="hero-subtitle">
          Book tickets, see your full booking details, and track travelled status.
        </p>
      </section>

      <section className="panel user-booking-panel">
        <div className="user-panel-head">
          <h2>Create New Booking</h2>
          <p className="hint">Choose source and destination stops to generate your ticket.</p>
        </div>
        <BookingForm
          sourceId={sourceId}
          destinationId={destinationId}
          stops={stops}
          booking={booking}
          loadingStops={loadingStops}
          canBook={canBook}
          onSourceChange={setSourceId}
          onDestinationChange={setDestinationId}
          onSubmit={handleBooking}
        />
      </section>

      {loadingStops && <p className="hint">Loading stops...</p>}
      {error && <p className="error">{error}</p>}
      <BookingResult bookingResult={bookingResult} />

      <section className="panel user-history-panel">
        <div className="result-header user-history-head">
          <h2>My Booking History</h2>
          <input
            value={searchText}
            onChange={(event) => setSearchText(event.target.value)}
            placeholder="Search by booking id, stop, or QR..."
          />
        </div>
        {loadingBookings && <p className="hint">Loading bookings...</p>}
        <div className="table-head user-bookings-head">
          <span>Booking</span>
          <span>Trip</span>
          <span>Status</span>
          <span>Actions</span>
        </div>
        <div className="list-block">
          {filteredBookings.map((item) => (
            <article className="booking-history-card" key={item.bookingId}>
              <div className="table-row user-bookings-row">
                <span>
                  <UserIcon className="icon icon-xs" /> ID {item.bookingId}
                </span>
                <span>
                  {item.source?.name || item.sourceName} {"->"}{" "}
                  {item.destination?.name || item.destinationName}
                </span>
                <span
                  className={(item.ticket?.travelled ?? item.travelled) ? "status-chip done" : "status-chip pending"}
                >
                  {(item.ticket?.travelled ?? item.travelled) ? "Travelled" : "Not Travelled"}
                </span>
                <div className="history-actions">
                  <button
                    className="primary-btn"
                    disabled={busyBookingId === item.bookingId || (item.ticket?.travelled ?? item.travelled)}
                    onClick={() => markTravelStatus(item.bookingId, true)}
                  >
                    Mark Travelled
                  </button>
                  <button
                    className="danger-btn"
                    disabled={busyBookingId === item.bookingId || !(item.ticket?.travelled ?? item.travelled)}
                    onClick={() => markTravelStatus(item.bookingId, false)}
                  >
                    Mark Not Travelled
                  </button>
                </div>
              </div>
              <div className="path-structured">
                <p className="path-title">Path</p>
                {(item.journey?.routeSegments || item.routeSegments || []).length > 0 ? (
                  (item.journey?.routeSegments || item.routeSegments || []).map((segment, index) => (
                    <div className="path-segment" key={`${item.bookingId}-${segment.routeId}-${index}`}>
                      <span className="path-line">{segment.routeColor} Line</span>
                      <span>{(segment.stops || []).join(" -> ")}</span>
                    </div>
                  ))
                ) : (
                  <p className="hint">No segment details available.</p>
                )}
              </div>
              <p className="hint">
                Stops: {item.journey?.totalStops ?? item.totalStops} | Transfers:{" "}
                {item.journey?.totalTransfers ?? item.totalTransfers}
              </p>
              <p className="hint">QR: {item.ticket?.qrString || item.qrString}</p>
            </article>
          ))}
          {!loadingBookings && filteredBookings.length === 0 && (
            <p className="hint">No bookings found.</p>
          )}
        </div>
      </section>
    </div>
  );
}
