package com.moveinsync.metro.repository;

import com.moveinsync.metro.entity.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {

    // Find stop by name (optional custom query)
    Optional<Stop> findByName(String name);

    // Check if stop exists by name
    boolean existsByName(String name);
}