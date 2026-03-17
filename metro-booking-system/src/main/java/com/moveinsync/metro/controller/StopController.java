package com.moveinsync.metro.controller;

import com.moveinsync.metro.entity.Stop;
import com.moveinsync.metro.service.StopService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stops")
public class StopController {

    private final StopService stopService;

    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    /**
     * GET all stops
     * Used by frontend dropdown
     */
    @GetMapping
    public ResponseEntity<List<Stop>> getAllStops() {
        return ResponseEntity.ok(stopService.getAllStops());
    }

    /**
     * GET stop by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Stop> getStopById(@PathVariable Long id) {
        return ResponseEntity.ok(stopService.getStopById(id));
    }

    /**
     * Create new stop (Admin API)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Stop> createStop(@RequestBody Stop stop) {
        return ResponseEntity.ok(stopService.createStop(stop));
    }

    /**
     * Update stop
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Stop> updateStop(
            @PathVariable Long id,
            @RequestBody Stop stop) {

        return ResponseEntity.ok(stopService.updateStop(id, stop));
    }

    /**
     * Delete stop
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStop(@PathVariable Long id) {
        stopService.deleteStop(id);
        return ResponseEntity.ok("Stop deleted successfully");
    }
}
