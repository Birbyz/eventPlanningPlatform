package com.example.eventplanner.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Venue;
import com.example.eventplanner.database.repositories.EventRepository;
import com.example.eventplanner.database.repositories.VenueRespository;

@Service
public class VenueService {
    @Autowired
    private VenueRespository venueRespository;

    @Autowired
    private EventRepository eventRepository;

    public List<Venue> getAllVenues() {
        return venueRespository.findAll();
    }

    public Venue addVenue(Venue venue) {
        return venueRespository.save(venue);
    }

    public Venue getVenueById(Long id) {
        return venueRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
    }

    public List<Venue> getVenuesByCountyId(Long countyId) {
        return venueRespository.findByCity_County_Id(countyId);
    }

    public List<Venue> getVenuesByCountyIdAndCityId(Long cityId, Long countyId) {
        return venueRespository.findByCity_IdAndCity_County_Id(cityId, countyId);
    }

    public void deleteVenueById(Long id) {
        venueRespository.deleteById(id);
    }

    public Boolean canDeleteVenue(Long id) {
        Venue venue = getVenueById(id);

        return eventRepository
                .findAll()
                .stream()
                .noneMatch(event -> event.getVenue() != null && event.getVenue().getId().equals(id));

    }
}
