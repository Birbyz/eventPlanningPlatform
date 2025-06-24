package com.example.eventplanner.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventplanner.database.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // methods which imply sorting, data manipulation (not operations)
    boolean existsByVenueId(Long venueId);
}
