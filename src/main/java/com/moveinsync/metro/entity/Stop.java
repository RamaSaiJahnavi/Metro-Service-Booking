package com.moveinsync.metro.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "stop")
    @JsonIgnore
    private Set<RouteStop> routeStops = new HashSet<>();

    public Stop() {
    }

    // Constructor used in DataLoader
    public Stop(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Full constructor
    public Stop(Long id, String name, Set<RouteStop> routeStops) {
        this.id = id;
        this.name = name;
        this.routeStops = routeStops;
    }

    // Getters & Setters

}
