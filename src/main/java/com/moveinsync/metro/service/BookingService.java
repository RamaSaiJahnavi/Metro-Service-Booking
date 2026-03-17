package com.moveinsync.metro.service;

import com.moveinsync.metro.dto.BookingRequestDTO;
import com.moveinsync.metro.dto.BookingResponseDTO;
import com.moveinsync.metro.dto.RouteSegmentDTO;
import com.moveinsync.metro.entity.AppUser;
import com.moveinsync.metro.entity.Booking;
import com.moveinsync.metro.exception.CustomException;
import com.moveinsync.metro.graph.Edge;
import com.moveinsync.metro.repository.AppUserRepository;
import com.moveinsync.metro.repository.BookingRepository;
import com.moveinsync.metro.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final StopService stopService;
    private final GraphService graphService;
    private final PathOptimizationService pathService;
    private final BookingRepository bookingRepository;
    private final RouteRepository routeRepository;
    private final AppUserRepository appUserRepository;

    public BookingService(
            StopService stopService,
            GraphService graphService,
            PathOptimizationService pathService,
            BookingRepository bookingRepository,
            RouteRepository routeRepository,
            AppUserRepository appUserRepository
    ) {
        this.stopService = stopService;
        this.graphService = graphService;
        this.pathService = pathService;
        this.bookingRepository = bookingRepository;
        this.routeRepository = routeRepository;
        this.appUserRepository = appUserRepository;
    }

    public BookingResponseDTO createBooking(BookingRequestDTO request, String username) {
        Long sourceId = request.getSourceId();
        Long destinationId = request.getDestinationId();

        if (sourceId.equals(destinationId)) {
            throw new CustomException("Source and destination cannot be same", "INVALID_SOURCE_DESTINATION");
        }

        stopService.getStopById(sourceId);
        stopService.getStopById(destinationId);

        List<Long> path = pathService.findShortestPath(graphService.getGraph(), sourceId, destinationId);
        if (path == null || path.isEmpty()) {
            throw new CustomException("No route found between selected stops", "ROUTE_NOT_FOUND");
        }

        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found", "USER_NOT_FOUND"));

        Booking booking = new Booking();
        booking.setSourceId(sourceId);
        booking.setDestinationId(destinationId);
        booking.setUser(user);
        booking.setPathJson(pathToJson(path));
        booking.setCreatedAt(LocalDateTime.now());
        booking.setQrString(UUID.randomUUID().toString());
        booking.setTravelled(false);
        bookingRepository.save(booking);

        return toResponse(booking);
    }

    public BookingResponseDTO getBookingResponseByIdForUser(Long bookingId, String username, boolean admin) {
        Booking booking = getBookingById(bookingId);
        if (!admin && !booking.getUser().getUsername().equals(username)) {
            throw new CustomException("You are not allowed to access this booking", "FORBIDDEN_BOOKING_ACCESS");
        }
        return toResponse(booking);
    }

    public List<BookingResponseDTO> getBookingsForUser(String username) {
        return bookingRepository.findByUserUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(this::toResponse)
                .toList();
    }

    public BookingResponseDTO updateTravelStatusForUser(Long bookingId, String username, boolean travelled) {
        Booking booking = getBookingById(bookingId);
        if (!booking.getUser().getUsername().equals(username)) {
            throw new CustomException("You are not allowed to update this booking", "FORBIDDEN_BOOKING_ACCESS");
        }
        booking.setTravelled(travelled);
        bookingRepository.save(booking);
        return toResponse(booking);
    }

    public BookingResponseDTO updateTravelStatusForAdmin(Long bookingId, boolean travelled) {
        Booking booking = getBookingById(bookingId);
        booking.setTravelled(travelled);
        bookingRepository.save(booking);
        return toResponse(booking);
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException("Booking not found with id: " + bookingId, "BOOKING_NOT_FOUND"));
    }

    private BookingResponseDTO toResponse(Booking booking) {
        List<Long> path = parsePathJson(booking.getPathJson());
        List<RouteSegmentDTO> routeSegments = buildRouteSegments(path, graphService.getGraph());
        int totalStops = path.size();
        int totalTransfers = Math.max(routeSegments.size() - 1, 0);
        String sourceName = stopService.getStopById(booking.getSourceId()).getName();
        String destinationName = stopService.getStopById(booking.getDestinationId()).getName();

        BookingResponseDTO response = new BookingResponseDTO(
                booking.getId(),
                booking.getUser().getUsername(),
                booking.getSourceId(),
                sourceName,
                booking.getDestinationId(),
                destinationName,
                path,
                routeSegments,
                totalStops,
                totalTransfers,
                booking.getQrString(),
                booking.isTravelled(),
                booking.getCreatedAt()
        );

        response.setProfile(new BookingResponseDTO.ProfileInfo(booking.getUser().getUsername()));
        response.setSource(new BookingResponseDTO.StopInfo(booking.getSourceId(), sourceName));
        response.setDestination(new BookingResponseDTO.StopInfo(booking.getDestinationId(), destinationName));
        response.setJourney(new BookingResponseDTO.JourneyInfo(path, routeSegments, totalStops, totalTransfers));
        response.setTicket(new BookingResponseDTO.TicketInfo(
                booking.getQrString(),
                booking.isTravelled(),
                booking.getCreatedAt()
        ));

        return response;
    }

    private String pathToJson(List<Long> path) {
        return path.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private List<Long> parsePathJson(String pathJson) {
        if (pathJson == null || pathJson.isBlank()) {
            return List.of();
        }
        String cleaned = pathJson.replace("[", "").replace("]", "").trim();
        if (cleaned.isBlank()) {
            return List.of();
        }
        List<Long> result = new ArrayList<>();
        for (String part : cleaned.split(",")) {
            String value = part.trim();
            if (!value.isEmpty()) {
                result.add(Long.parseLong(value));
            }
        }
        return result;
    }

    private List<RouteSegmentDTO> buildRouteSegments(List<Long> path, Map<Long, List<Edge>> graph) {
        List<RouteSegmentDTO> segments = new ArrayList<>();
        if (path.size() < 2) {
            return segments;
        }

        Long activeRouteId = null;
        List<String> activeStops = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            Long from = path.get(i);
            Long to = path.get(i + 1);
            Long routeId = getRouteIdForHop(from, to, graph);
            String fromName = stopService.getStopById(from).getName();
            String toName = stopService.getStopById(to).getName();

            if (activeRouteId == null || !activeRouteId.equals(routeId)) {
                if (!activeStops.isEmpty() && activeRouteId != null) {
                    segments.add(new RouteSegmentDTO(activeRouteId, resolveRouteColor(activeRouteId), new ArrayList<>(activeStops)));
                }
                activeRouteId = routeId;
                activeStops = new ArrayList<>();
                activeStops.add(fromName);
            }

            if (activeStops.isEmpty() || !activeStops.get(activeStops.size() - 1).equals(toName)) {
                activeStops.add(toName);
            }
        }

        if (!activeStops.isEmpty() && activeRouteId != null) {
            segments.add(new RouteSegmentDTO(activeRouteId, resolveRouteColor(activeRouteId), activeStops));
        }

        return segments;
    }

    private Long getRouteIdForHop(Long from, Long to, Map<Long, List<Edge>> graph) {
        return graph.getOrDefault(from, List.of()).stream()
                .filter(edge -> edge.getTo().equals(to))
                .map(Edge::getRouteId)
                .findFirst()
                .orElse(null);
    }

    private String resolveRouteColor(Long routeId) {
        if (routeId == null) {
            return "Unknown";
        }
        return routeRepository.findById(routeId).map(route -> route.getColor()).orElse("Unknown");
    }
}
