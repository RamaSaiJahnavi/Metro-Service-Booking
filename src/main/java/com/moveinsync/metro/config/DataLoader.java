package com.moveinsync.metro.config;

import com.moveinsync.metro.entity.Route;
import com.moveinsync.metro.entity.RouteStop;
import com.moveinsync.metro.entity.Stop;
import com.moveinsync.metro.repository.RouteRepository;
import com.moveinsync.metro.repository.RouteStopRepository;
import com.moveinsync.metro.repository.StopRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;
    private final RouteStopRepository routeStopRepository;

    public DataLoader(StopRepository stopRepository,
                      RouteRepository routeRepository,
                      RouteStopRepository routeStopRepository) {
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
        this.routeStopRepository = routeStopRepository;
    }

    @Override
    public void run(String... args) {

        if (stopRepository.count() > 0) {
            return; // prevent duplicate data
        }

        // ---------- Create Stops ----------
        Stop a = stopRepository.save(new Stop(null, "Vijayawada"));
        Stop b = stopRepository.save(new Stop(null, "Guntur"));
        Stop c = stopRepository.save(new Stop(null, "Tenali"));
        Stop d = stopRepository.save(new Stop(null, "Mangalagiri"));
        Stop e = stopRepository.save(new Stop(null, "Amaravati"));

        // ---------- Create Routes ----------
        Route yellow = routeRepository.save(new Route(null, "Yellow"));
        Route blue = routeRepository.save(new Route(null, "Blue"));

        // ---------- Yellow Line ----------
        routeStopRepository.save(new RouteStop(null, yellow, a, 1));
        routeStopRepository.save(new RouteStop(null, yellow, b, 2));
        routeStopRepository.save(new RouteStop(null, yellow, c, 3));

        // ---------- Blue Line ----------
        routeStopRepository.save(new RouteStop(null, blue, c, 1));
        routeStopRepository.save(new RouteStop(null, blue, d, 2));
        routeStopRepository.save(new RouteStop(null, blue, e, 3));
    }
}
