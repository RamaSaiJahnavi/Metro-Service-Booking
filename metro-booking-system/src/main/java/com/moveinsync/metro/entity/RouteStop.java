package com.moveinsync.metro.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne
    @JoinColumn(name = "stop_id", nullable = false)
    private Stop stop;

    private int stopOrder;

    // Default constructor (Required by JPA)
    public RouteStop() {
    }

    // Constructor WITHOUT id (Recommended)
    public RouteStop(Route route, Stop stop, int stopOrder) {
        this.route = route;
        this.stop = stop;
        this.stopOrder = stopOrder;
    }

    // Full constructor (Optional)
    public RouteStop(Long id, Route route, Stop stop, int stopOrder) {
        this.id = id;
        this.route = route;
        this.stop = stop;
        this.stopOrder = stopOrder;
    }

    // Getters and Setters


}
