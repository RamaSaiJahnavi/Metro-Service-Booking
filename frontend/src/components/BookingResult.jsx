import { QRCodeSVG } from "qrcode.react";

const ROUTE_COLORS = {
  RED: "#e63946",
  BLUE: "#1d4ed8",
  GREEN: "#16a34a",
  YELLOW: "#f59e0b",
  ORANGE: "#f97316"
};

function resolveRouteColor(routeColor) {
  if (!routeColor) return "#334155";
  const key = String(routeColor).toUpperCase();
  return ROUTE_COLORS[key] || routeColor.toLowerCase();
}

function formatDateTime(value) {
  if (!value) return "-";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return value;
  return date.toLocaleString();
}

export default function BookingResult({ bookingResult }) {
  if (!bookingResult) return null;

  return (
    <section className="panel result-panel">
      <div className="result-header">
        <h2>Booking Confirmed</h2>
        <span className="booking-id">ID {bookingResult.bookingId}</span>
      </div>

      <div className="stat-grid">
        <article>
          <p>Total Stops</p>
          <strong>{bookingResult.totalStops}</strong>
        </article>
        <article>
          <p>Total Transfers</p>
          <strong>{bookingResult.totalTransfers}</strong>
        </article>
        <article>
          <p>Booked At</p>
          <strong>{formatDateTime(bookingResult.createdAt)}</strong>
        </article>
      </div>

      <div className="segments">
        <h3>Route Segments</h3>
        {(bookingResult.routeSegments || []).map((segment, index) => (
          <article className="segment-card" key={`${segment.routeId}-${index}`}>
            <header>
              <span
                className="route-chip"
                style={{ backgroundColor: resolveRouteColor(segment.routeColor) }}
              >
                {segment.routeColor} Line
              </span>
              <span>Route {segment.routeId}</span>
            </header>
            <p>{(segment.stops || []).join("  ->  ")}</p>
          </article>
        ))}
      </div>

      <div className="qr-block">
        <div>
          <h3>QR Ticket</h3>
          <p className="hint">Scan this QR payload for verification at metro gates.</p>
          <code>{bookingResult.qrString}</code>
        </div>
        <div className="qr-card">
          <QRCodeSVG value={bookingResult.qrString || "metro-ticket"} size={140} />
        </div>
      </div>
    </section>
  );
}
