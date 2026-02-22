package com.moveinsync.metro.service;

import com.moveinsync.metro.entity.Stop;
import com.moveinsync.metro.exception.CustomException;
import com.moveinsync.metro.repository.StopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopService {

    private final StopRepository stopRepository;
    private final GraphService graphService;

    public StopService(StopRepository stopRepository, GraphService graphService) {
        this.stopRepository = stopRepository;
        this.graphService = graphService;
    }

    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }

    public Stop getStopById(Long id) {
        return stopRepository.findById(id)
                .orElseThrow(() -> new CustomException("Stop not found", "STOP_NOT_FOUND"));
    }

    public Stop createStop(Stop stop) {
        Stop saved = stopRepository.save(stop);
        graphService.refreshGraph();
        return saved;
    }

    public Stop updateStop(Long id, Stop updatedStop) {
        Stop stop = getStopById(id);
        stop.setName(updatedStop.getName());
        Stop saved = stopRepository.save(stop);
        graphService.refreshGraph();
        return saved;
    }

    public void deleteStop(Long id) {
        stopRepository.deleteById(id);
        graphService.refreshGraph();
    }
}
