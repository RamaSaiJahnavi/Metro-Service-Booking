package com.moveinsync.metro.service;

import com.moveinsync.metro.dto.AdminRouteRequestDTO;
import com.moveinsync.metro.dto.AdminRouteStopRequestDTO;
import com.moveinsync.metro.dto.AdminRouteStopResponseDTO;
import com.moveinsync.metro.dto.AdminStopRequestDTO;
import com.moveinsync.metro.dto.AdminUserResponseDTO;
import com.moveinsync.metro.dto.BookingResponseDTO;
import com.moveinsync.metro.entity.Route;
import com.moveinsync.metro.entity.RouteStop;
import com.moveinsync.metro.entity.Stop;
import com.moveinsync.metro.exception.CustomException;
import com.moveinsync.metro.repository.AppUserRepository;
import com.moveinsync.metro.repository.RouteRepository;
import com.moveinsync.metro.repository.RouteStopRepository;
import com.moveinsync.metro.repository.StopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class AdminMetroService {

    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;
    private final GraphService graphService;
    private final AppUserRepository appUserRepository;
    private final BookingService bookingService;

    public AdminMetroService(
            StopRepository stopRepository,
            RouteRepository routeRepository,
            RouteStopRepository routeStopRepository,
            GraphService graphService,
            AppUserRepository appUserRepository,
            BookingService bookingService
    ) {
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
        this.graphService = graphService;
        this.appUserRepository = appUserRepository;
        this.bookingService = bookingService;
    }

    @Transactional
    public Stop createStop(AdminStopRequestDTO request) {
        Stop stop = new Stop();
        stop.setName(request.getName().trim());
        Stop saved = stopRepository.save(stop);
        graphService.refreshGraph();
        return saved;
    }

    @Transactional
    public Stop updateStop(Long id, AdminStopRequestDTO request) {
        Stop stop = stopRepository.findById(id)
                .orElseThrow(() -> new CustomException("Stop not found", "STOP_NOT_FOUND"));
        stop.setName(request.getName().trim());
        Stop saved = stopRepository.save(stop);
        graphService.refreshGraph();
        return saved;
    }

    @Transactional
    public void deleteStop(Long id) {
        Stop stop = stopRepository.findById(id)
                .orElseThrow(() -> new CustomException("Stop not found", "STOP_NOT_FOUND"));
        stopRepository.delete(stop);
        graphService.refreshGraph();
    }

    @Transactional
    public Route createRoute(AdminRouteRequestDTO request) {
        Route route = new Route();
        route.setColor(request.getColor().trim());
        Route saved = routeRepository.save(route);
        graphService.refreshGraph();
        return saved;
    }

    @Transactional
    public Route updateRoute(Long id, AdminRouteRequestDTO request) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Route not found", "ROUTE_NOT_FOUND"));
        route.setColor(request.getColor().trim());
        Route saved = routeRepository.save(route);
        graphService.refreshGraph();
        return saved;
    }

    @Transactional
    public void deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Route not found", "ROUTE_NOT_FOUND"));
        routeRepository.delete(route);
        graphService.refreshGraph();
    }

    @Transactional(readOnly = true)
    public List<AdminRouteStopResponseDTO> getRouteStops(Long routeId) {
        List<RouteStop> routeStops = routeId == null
                ? routeStopRepository.findAll()
                : routeStopRepository.findByRouteIdOrderByStopOrderAsc(routeId);

        return routeStops.stream()
                .sorted(Comparator.comparing((RouteStop rs) -> rs.getRoute().getId())
                        .thenComparing(RouteStop::getStopOrder))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AdminRouteStopResponseDTO createRouteStop(AdminRouteStopRequestDTO request) {
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new CustomException("Route not found", "ROUTE_NOT_FOUND"));
        Stop stop = stopRepository.findById(request.getStopId())
                .orElseThrow(() -> new CustomException("Stop not found", "STOP_NOT_FOUND"));

        RouteStop routeStop = new RouteStop();
        routeStop.setRoute(route);
        routeStop.setStop(stop);
        routeStop.setStopOrder(request.getStopOrder());
        RouteStop saved = routeStopRepository.save(routeStop);
        graphService.refreshGraph();
        return toResponse(saved);
    }

    @Transactional
    public AdminRouteStopResponseDTO updateRouteStop(Long id, AdminRouteStopRequestDTO request) {
        RouteStop routeStop = routeStopRepository.findById(id)
                .orElseThrow(() -> new CustomException("Route stop mapping not found", "ROUTE_STOP_NOT_FOUND"));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new CustomException("Route not found", "ROUTE_NOT_FOUND"));
        Stop stop = stopRepository.findById(request.getStopId())
                .orElseThrow(() -> new CustomException("Stop not found", "STOP_NOT_FOUND"));

        routeStop.setRoute(route);
        routeStop.setStop(stop);
        routeStop.setStopOrder(request.getStopOrder());
        RouteStop saved = routeStopRepository.save(routeStop);
        graphService.refreshGraph();
        return toResponse(saved);
    }

    @Transactional
    public void deleteRouteStop(Long id) {
        RouteStop routeStop = routeStopRepository.findById(id)
                .orElseThrow(() -> new CustomException("Route stop mapping not found", "ROUTE_STOP_NOT_FOUND"));
        routeStopRepository.delete(routeStop);
        graphService.refreshGraph();
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponseDTO> getUsers() {
        return appUserRepository.findAll().stream()
                .map(user -> new AdminUserResponseDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @Transactional
    public BookingResponseDTO updateBookingTravelStatus(Long bookingId, boolean travelled) {
        return bookingService.updateTravelStatusForAdmin(bookingId, travelled);
    }

    private AdminRouteStopResponseDTO toResponse(RouteStop routeStop) {
        return new AdminRouteStopResponseDTO(
                routeStop.getId(),
                routeStop.getRoute().getId(),
                routeStop.getRoute().getColor(),
                routeStop.getStop().getId(),
                routeStop.getStop().getName(),
                routeStop.getStopOrder()
        );
    }
}
