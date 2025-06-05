package com.example.eventplanner.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.services.VenueService;

@RestController
@RequestMapping("/venues")
public class VenueController {
    
    @Autowired
    private VenueService venueService;
}
