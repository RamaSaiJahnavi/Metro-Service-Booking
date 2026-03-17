package com.moveinsync.metro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AdminRouteStopRequestDTO {

    @NotNull(message = "Route ID is required")
    private Long routeId;

    @NotNull(message = "Stop ID is required")
    private Long stopId;

    @Min(value = 1, message = "Stop order must be at least 1")
    private int stopOrder;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getStopId() {
        return stopId;
    }

    public void setStopId(Long stopId) {
        this.stopId = stopId;
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }
}
