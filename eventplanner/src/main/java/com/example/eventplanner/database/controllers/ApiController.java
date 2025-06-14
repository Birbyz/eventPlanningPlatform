package com.example.eventplanner.database.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.entities.City;
import com.example.eventplanner.database.entities.Venue;
import com.example.eventplanner.database.services.CityService;
import com.example.eventplanner.database.services.VenueService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CityService cityService;

    @Autowired
    private VenueService venueService;

    @GetMapping("/cities")
    public List<City> getCitiesByCountyId(@RequestParam Long countyId) {
        return cityService.getCitiesByCountyId(countyId);
    }

    @GetMapping("/venue/{venueId}")
    public Map<String, Long> getVenueLocation(@PathVariable Long venueId) {
        Venue venue = venueService.getVenueById(venueId);

        Long cityId = venue.getCity().getId();
        Long countyId = venue.getCity().getCounty().getId();

        Map<String, Long> response = new HashMap<>();
        response.put("cityId", cityId);
        response.put("countyId", countyId);

        return response;
    }

    @GetMapping("/venues")
    public List<Venue> getVenuesByCountyId (@RequestParam Long countyId) {
        return venueService.getVenuesByCountyId(countyId);
    }

    @GetMapping("/venues/get-by-county-and-city")
    public List<Venue> getVenuesByCountyAndCity(
        @RequestParam Long countyId,
        @RequestParam Long cityId
    ) {
        return venueService.getVenuesByCountyIdAndCityId(cityId, countyId);
    }
}
