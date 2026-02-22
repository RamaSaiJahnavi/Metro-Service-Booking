package com.moveinsync.metro.dto;

import java.util.List;

public class RouteSegmentDTO {

    private Long routeId;
    private String routeColor;
    private List<String> stops;

    public RouteSegmentDTO() {}

    public RouteSegmentDTO(Long routeId, String routeColor, List<String> stops) {
        this.routeId = routeId;
        this.routeColor = routeColor;
        this.stops = stops;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public List<String> getStops() {
        return stops;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setRouteColor(String routeColor) {
        this.routeColor = routeColor;
    }

    public void setStops(List<String> stops) {
        this.stops = stops;
    }
}