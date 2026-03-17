package com.moveinsync.metro.repository;

import com.moveinsync.metro.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    // Get all stops of a route ordered by stop order
    List<RouteStop> findByRouteIdOrderByStopOrderAsc(Long routeId);

    // Get all route mappings for a stop
    List<RouteStop> findByStopId(Long stopId);
}
