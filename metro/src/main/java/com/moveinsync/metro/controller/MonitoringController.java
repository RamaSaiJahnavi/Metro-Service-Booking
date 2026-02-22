package com.moveinsync.metro.controller;

import com.moveinsync.metro.service.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MonitoringController {

    private final GraphService graphService;

    public MonitoringController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString(),
                "uptimeMs", uptimeMs,
                "graphNodes", graphService.getNodeCount(),
                "graphEdges", graphService.getEdgeCount()
        ));
    }
}
