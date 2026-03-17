package com.moveinsync.metro.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<RouteStop> routeStops = new HashSet<>();

    // =========================
    // Constructors
    // =========================

    // Default constructor (Required by JPA)
    public Route() {
    }

    // Constructor used in DataLoader
    public Route(Long id, String color) {
        this.id = id;
        this.color = color;
    }

    // Optional full constructor
    public Route(Long id, String color, Set<RouteStop> routeStops) {
        this.id = id;
        this.color = color;
        this.routeStops = routeStops;
    }

    // =========================
    // Getters & Setters
    // =========================

    // =========================
    // Helper Methods (Optional but Good Practice)
    // =========================

    public void addRouteStop(RouteStop routeStop) {
        routeStops.add(routeStop);
        routeStop.setRoute(this);
    }

    public void removeRouteStop(RouteStop routeStop) {
        routeStops.remove(routeStop);
        routeStop.setRoute(null);
    }
}
