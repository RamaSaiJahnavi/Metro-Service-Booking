package com.moveinsync.metro.repository;

import com.moveinsync.metro.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    // Optional: find by color
    Route findByColor(String color);
}