package com.example.eventplanner.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Venue;
import com.example.eventplanner.database.repositories.VenueRespository;

@Service
public class VenueService {
    @Autowired
    private VenueRespository venueRespository;

    public List<Venue> getAllVenues() {
        return venueRespository.findAll();
    }

    public Venue addVenue(Venue venue) {
        return venueRespository.save(venue);
    }
}
