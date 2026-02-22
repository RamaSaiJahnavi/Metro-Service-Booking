import { TicketIcon } from "./Icons";

export default function BookingForm({
  sourceId,
  destinationId,
  stops,
  booking,
  loadingStops,
  canBook,
  onSourceChange,
  onDestinationChange,
  onSubmit
}) {
  return (
    <form className="booking-form user-booking-form" onSubmit={onSubmit}>
      <label>
        Source Stop
        <select
          value={sourceId}
          onChange={(event) => onSourceChange(event.target.value)}
          disabled={loadingStops || booking}
          required
        >
          <option value="">Select source</option>
          {stops.map((stop) => (
            <option key={stop.id} value={stop.id}>
              {stop.name}
            </option>
          ))}
        </select>
      </label>

      <label>
        Destination Stop
        <select
          value={destinationId}
          onChange={(event) => onDestinationChange(event.target.value)}
          disabled={loadingStops || booking}
          required
        >
          <option value="">Select destination</option>
          {stops.map((stop) => (
            <option key={stop.id} value={stop.id}>
              {stop.name}
            </option>
          ))}
        </select>
      </label>

      <button className="primary-btn" type="submit" disabled={!canBook}>
        <TicketIcon className="icon icon-xs" />
        {booking ? "Booking..." : "Book Ticket"}
      </button>
    </form>
  );
}
