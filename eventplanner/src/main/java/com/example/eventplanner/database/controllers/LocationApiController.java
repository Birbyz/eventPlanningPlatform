package com.example.eventplanner.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.entities.City;
import com.example.eventplanner.database.services.CityService;

@RestController
@RequestMapping("/api")
public class LocationApiController {

    @Autowired
    private CityService cityService;

    @GetMapping("/cities")
    public List<City> getCitiesByCountyId(@RequestParam Long countyId) {
        return cityService.getCitiesByCountyId(countyId);
    }
}
