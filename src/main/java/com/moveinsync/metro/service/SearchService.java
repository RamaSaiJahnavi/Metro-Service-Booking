package com.moveinsync.metro.service;

import com.moveinsync.metro.dto.BookingResponseDTO;
import com.moveinsync.metro.repository.AppUserRepository;
import com.moveinsync.metro.repository.RouteRepository;
import com.moveinsync.metro.repository.StopRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final AppUserRepository appUserRepository;
    private final AdminMetroService adminMetroService;
    private final BookingService bookingService;

    public SearchService(
            StopRepository stopRepository,
            RouteRepository routeRepository,
            AppUserRepository appUserRepository,
            AdminMetroService adminMetroService,
            BookingService bookingService
    ) {
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
        this.appUserRepository = appUserRepository;
        this.adminMetroService = adminMetroService;
        this.bookingService = bookingService;
    }

    public Map<String, Object> search(String query, String username, boolean admin) {
        String q = query == null ? "" : query.trim().toLowerCase();

        List<Map<String, Object>> stops = stopRepository.findAll().stream()
                .filter(stop -> matches(q, stop.getName(), String.valueOf(stop.getId())))
                .map(stop -> mapOf(
                        "id", stop.getId(),
                        "name", stop.getName()
                ))
                .toList();

        List<Map<String, Object>> routes = routeRepository.findAll().stream()
                .filter(route -> matches(q, route.getColor(), String.valueOf(route.getId())))
                .map(route -> mapOf(
                        "id", route.getId(),
                        "color", route.getColor()
                ))
                .toList();

        List<BookingResponseDTO> bookingPool = admin ? bookingService.getAllBookings() : bookingService.getBookingsForUser(username);
        List<BookingResponseDTO> bookings = bookingPool.stream()
                .filter(booking -> matches(
                        q,
                        booking.getQrString(),
                        booking.getSourceName(),
                        booking.getDestinationName(),
                        booking.getUsername(),
                        String.valueOf(booking.getBookingId())
                ))
                .toList();

        if (!admin) {
            return Map.of(
                    "query", query,
                    "stops", stops,
                    "routes", routes,
                    "bookings", bookings
            );
        }

        List<Map<String, Object>> users = appUserRepository.findAll().stream()
                .filter(user -> matches(q, user.getUsername(), user.getEmail(), user.getRole().name(), String.valueOf(user.getId())))
                .map(user -> mapOf(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole().name()
                ))
                .toList();

        List<Map<String, Object>> routeStops = adminMetroService.getRouteStops(null).stream()
                .filter(mapping -> matches(
                        q,
                        mapping.getRouteColor(),
                        mapping.getStopName(),
                        String.valueOf(mapping.getRouteId()),
                        String.valueOf(mapping.getStopId()),
                        String.valueOf(mapping.getId())
                ))
                .map(mapping -> mapOf(
                        "id", mapping.getId(),
                        "routeId", mapping.getRouteId(),
                        "routeColor", mapping.getRouteColor(),
                        "stopId", mapping.getStopId(),
                        "stopName", mapping.getStopName(),
                        "stopOrder", mapping.getStopOrder()
                ))
                .collect(Collectors.toList());

        return Map.of(
                "query", query,
                "stops", stops,
                "routes", routes,
                "bookings", bookings,
                "users", users,
                "routeStops", routeStops
        );
    }

    private boolean matches(String query, String... values) {
        if (query == null || query.isBlank()) {
            return true;
        }
        for (String value : values) {
            if (value != null && value.toLowerCase().contains(query)) {
                return true;
            }
        }
        return false;
    }

    private Map<String, Object> mapOf(
            String key1, Object value1,
            String key2, Object value2
    ) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    private Map<String, Object> mapOf(
            String key1, Object value1,
            String key2, Object value2,
            String key3, Object value3,
            String key4, Object value4
    ) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        return map;
    }

    private Map<String, Object> mapOf(
            String key1, Object value1,
            String key2, Object value2,
            String key3, Object value3,
            String key4, Object value4,
            String key5, Object value5,
            String key6, Object value6
    ) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        map.put(key5, value5);
        map.put(key6, value6);
        return map;
    }
}
