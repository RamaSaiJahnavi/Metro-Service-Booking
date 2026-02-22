package com.moveinsync.metro.service;

import com.moveinsync.metro.entity.Route;
import com.moveinsync.metro.exception.CustomException;
import com.moveinsync.metro.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final GraphService graphService;

    public RouteService(RouteRepository routeRepository, GraphService graphService) {
        this.routeRepository = routeRepository;
        this.graphService = graphService;
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new CustomException("Route not found", "ROUTE_NOT_FOUND"));
    }

    public Route createRoute(Route route) {
        Route saved = routeRepository.save(route);
        graphService.refreshGraph();
        return saved;
    }

    public Route updateRoute(Long id, Route updatedRoute) {
        Route route = getRouteById(id);
        route.setColor(updatedRoute.getColor());
        Route saved = routeRepository.save(route);
        graphService.refreshGraph();
        return saved;
    }

    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
        graphService.refreshGraph();
    }
}
