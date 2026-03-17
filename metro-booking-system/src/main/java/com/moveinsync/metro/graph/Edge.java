package com.moveinsync.metro.graph;

public class Edge {

    private Long to;          // Destination stop ID
    private int weight;       // Travel cost (default 1 for now)
    private Long routeId;     // Route this edge belongs to

    public Edge(Long to, int weight, Long routeId) {
        this.to = to;
        this.weight = weight;
        this.routeId = routeId;
    }

    public Long getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    public Long getRouteId() {
        return routeId;
    }
}