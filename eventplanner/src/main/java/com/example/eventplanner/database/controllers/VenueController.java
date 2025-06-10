package com.example.eventplanner.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.entities.Venue;
import com.example.eventplanner.database.services.CityService;
import com.example.eventplanner.database.services.CountyService;
import com.example.eventplanner.database.services.VenueService;

@Controller
@RequestMapping("/venues")
public class VenueController {
    
    @Autowired
    private VenueService venueService;

    @Autowired
    private CountyService countyService;

    @Autowired
    private CityService cityService;

    @GetMapping()
    public String showVenuesScreen(Model model) {
        model.addAttribute("venues", venueService.getAllVenues());
        
        return "venues";
    }
    
    // ADD
    @GetMapping("/add")
    public String showAddVenueForm(Model model) {
        model.addAttribute("venue", new Venue());
        model.addAttribute("counties", countyService.getAllCounties());

        return "add-venue-form";
    }
}
