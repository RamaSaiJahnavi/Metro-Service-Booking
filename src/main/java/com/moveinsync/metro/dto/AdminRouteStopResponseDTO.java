package com.moveinsync.metro.dto;

public class AdminRouteStopResponseDTO {

    private Long id;
    private Long routeId;
    private String routeColor;
    private Long stopId;
    private String stopName;
    private int stopOrder;

    public AdminRouteStopResponseDTO(Long id, Long routeId, String routeColor, Long stopId, String stopName, int stopOrder) {
        this.id = id;
        this.routeId = routeId;
        this.routeColor = routeColor;
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopOrder = stopOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public Long getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public int getStopOrder() {
        return stopOrder;
    }
}
