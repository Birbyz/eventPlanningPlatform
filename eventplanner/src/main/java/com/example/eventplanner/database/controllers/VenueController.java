package com.example.eventplanner.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventplanner.database.entities.Venue;
import com.example.eventplanner.database.services.CityService;
import com.example.eventplanner.database.services.CountyService;
import com.example.eventplanner.database.services.VenueService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/venues")
public class VenueController {
    
    @Autowired
    private VenueService venueService;

    @Autowired
    private CountyService countyService;

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

    @PostMapping("/add")
    public String addVenue(@Valid @ModelAttribute Venue venue, BindingResult result, Model model) {
        System.out.println("ENTER: add venue fun");

        if (result.hasErrors()) {
            System.out.println("VALIDATION ERRORS: " + result.getAllErrors());
            // reset values
            model.addAttribute("counties", countyService.getAllCounties());

            return "add-venue-form";
        }

        try {
            System.out.println("TRY");

            Venue newVenue = new Venue();
            newVenue.setName(venue.getName());
            newVenue.setAddress(venue.getAddress());
            newVenue.setCity(venue.getCity());

            venueService.addVenue(newVenue);

        } catch (Exception e){
            model.addAttribute("savingError", "Unable to save venue: " + e.getMessage());
            model.addAttribute("counties", countyService.getAllCounties());

            return "add-venue-form";
        }

        return "redirect:/venues";
    }
}
