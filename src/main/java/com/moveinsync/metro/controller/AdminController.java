package com.moveinsync.metro.controller;

import com.moveinsync.metro.dto.AdminRouteRequestDTO;
import com.moveinsync.metro.dto.AdminRouteStopRequestDTO;
import com.moveinsync.metro.dto.AdminRouteStopResponseDTO;
import com.moveinsync.metro.dto.AdminStopRequestDTO;
import com.moveinsync.metro.dto.AdminUserResponseDTO;
import com.moveinsync.metro.dto.BookingResponseDTO;
import com.moveinsync.metro.dto.TravelStatusRequestDTO;
import com.moveinsync.metro.entity.Route;
import com.moveinsync.metro.entity.Stop;
import com.moveinsync.metro.service.AdminMetroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminMetroService adminMetroService;

    public AdminController(AdminMetroService adminMetroService) {
        this.adminMetroService = adminMetroService;
    }

    @PostMapping("/stops")
    public ResponseEntity<Stop> createStop(@Valid @RequestBody AdminStopRequestDTO request) {
        return ResponseEntity.ok(adminMetroService.createStop(request));
    }

    @PutMapping("/stops/{id}")
    public ResponseEntity<Stop> updateStop(
            @PathVariable Long id,
            @Valid @RequestBody AdminStopRequestDTO request
    ) {
        return ResponseEntity.ok(adminMetroService.updateStop(id, request));
    }

    @DeleteMapping("/stops/{id}")
    public ResponseEntity<Map<String, String>> deleteStop(@PathVariable Long id) {
        adminMetroService.deleteStop(id);
        return ResponseEntity.ok(Map.of("message", "Stop deleted successfully"));
    }

    @PostMapping("/routes")
    public ResponseEntity<Route> createRoute(@Valid @RequestBody AdminRouteRequestDTO request) {
        return ResponseEntity.ok(adminMetroService.createRoute(request));
    }

    @PutMapping("/routes/{id}")
    public ResponseEntity<Route> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody AdminRouteRequestDTO request
    ) {
        return ResponseEntity.ok(adminMetroService.updateRoute(id, request));
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Map<String, String>> deleteRoute(@PathVariable Long id) {
        adminMetroService.deleteRoute(id);
        return ResponseEntity.ok(Map.of("message", "Route deleted successfully"));
    }

    @GetMapping("/route-stops")
    public ResponseEntity<List<AdminRouteStopResponseDTO>> getRouteStops(
            @RequestParam(required = false) Long routeId
    ) {
        return ResponseEntity.ok(adminMetroService.getRouteStops(routeId));
    }

    @PostMapping("/route-stops")
    public ResponseEntity<AdminRouteStopResponseDTO> createRouteStop(
            @Valid @RequestBody AdminRouteStopRequestDTO request
    ) {
        return ResponseEntity.ok(adminMetroService.createRouteStop(request));
    }

    @PutMapping("/route-stops/{id}")
    public ResponseEntity<AdminRouteStopResponseDTO> updateRouteStop(
            @PathVariable Long id,
            @Valid @RequestBody AdminRouteStopRequestDTO request
    ) {
        return ResponseEntity.ok(adminMetroService.updateRouteStop(id, request));
    }

    @DeleteMapping("/route-stops/{id}")
    public ResponseEntity<Map<String, String>> deleteRouteStop(@PathVariable Long id) {
        adminMetroService.deleteRouteStop(id);
        return ResponseEntity.ok(Map.of("message", "Route-stop mapping deleted successfully"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponseDTO>> getUsers() {
        return ResponseEntity.ok(adminMetroService.getUsers());
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponseDTO>> getBookings() {
        return ResponseEntity.ok(adminMetroService.getAllBookings());
    }

    @PutMapping("/bookings/{id}/travelled")
    public ResponseEntity<BookingResponseDTO> updateBookingTravel(
            @PathVariable Long id,
            @RequestBody TravelStatusRequestDTO request
    ) {
        return ResponseEntity.ok(adminMetroService.updateBookingTravelStatus(id, request.isTravelled()));
    }
}
