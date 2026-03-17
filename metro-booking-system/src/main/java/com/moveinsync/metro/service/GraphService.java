package com.moveinsync.metro.service;

import com.moveinsync.metro.entity.RouteStop;
import com.moveinsync.metro.repository.RouteStopRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.moveinsync.metro.graph.Edge;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GraphService {

    private final RouteStopRepository routeStopRepository;

    private final Map<Long, List<Edge>> graph = new ConcurrentHashMap<>();

    public GraphService(RouteStopRepository routeStopRepository) {
        this.routeStopRepository = routeStopRepository;
    }

    @PostConstruct
    public void buildGraph() {
        refreshGraph();
    }

    public synchronized void refreshGraph() {
        Map<Long, List<Edge>> newGraph = new HashMap<>();

        List<RouteStop> allRouteStops = routeStopRepository.findAll();

        Map<Long, List<RouteStop>> grouped =
                new HashMap<>();

        for (RouteStop rs : allRouteStops) {
            grouped.computeIfAbsent(
                    rs.getRoute().getId(),
                    k -> new ArrayList<>()
            ).add(rs);
        }

        for (List<RouteStop> stops : grouped.values()) {

            stops.sort(Comparator.comparing(RouteStop::getStopOrder));

            for (int i = 0; i < stops.size() - 1; i++) {

                Long from = stops.get(i).getStop().getId();
                Long to = stops.get(i + 1).getStop().getId();
                Long routeId = stops.get(i).getRoute().getId();

                newGraph.computeIfAbsent(from, k -> new ArrayList<>())
                        .add(new Edge(to, 1, routeId));

                newGraph.computeIfAbsent(to, k -> new ArrayList<>())
                        .add(new Edge(from, 1, routeId));
            }
        }

        graph.clear();
        graph.putAll(newGraph);
    }

    public Map<Long, List<Edge>> getGraph() {
        return graph;
    }

    public int getNodeCount() {
        return graph.size();
    }

    public int getEdgeCount() {
        int total = 0;
        for (List<Edge> edges : graph.values()) {
            total += edges.size();
        }
        return total;
    }
}
