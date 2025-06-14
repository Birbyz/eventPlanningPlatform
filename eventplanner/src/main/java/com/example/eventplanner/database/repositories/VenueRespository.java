package com.example.eventplanner.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventplanner.database.entities.Venue;

@Repository
public interface VenueRespository extends JpaRepository<Venue, Long> {
    
    // methods which imply sorting, data manipulation (not operations)
    List<Venue> findByCity_County_Id(Long countyId);

    List<Venue> findByCity_IdAndCity_County_Id(Long cityId, Long countyId);
}
