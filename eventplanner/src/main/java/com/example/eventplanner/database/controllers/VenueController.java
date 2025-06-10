package com.example.eventplanner.database.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private CityService cityService;

    @GetMapping()
    public String showVenuesScreen(
            @RequestParam(required = false) String sortByName,
            @RequestParam(required = false) String sortByCounty,
            @RequestParam(required = false) String sortByCity,
            Model model) {
        List<Venue> venues = venueService.getAllVenues();

        // filter by county
        if (sortByCounty != null && !sortByCounty.isBlank()) {
            venues = venues.stream()
                    .filter(v -> v.getCity().getCounty().getName().equalsIgnoreCase(sortByCounty))
                    .collect(Collectors.toList());
        }

        // filter by town
        if (sortByCity != null && !sortByCity.isBlank()) {
            venues = venues.stream()
                    .filter(v -> v.getCity().getName().equalsIgnoreCase(sortByCity))
                    .collect(Collectors.toList());
        }

        // alphabetical sort
        if ("asc".equalsIgnoreCase(sortByName)) {
            venues.sort(Comparator.comparing(Venue::getName, String.CASE_INSENSITIVE_ORDER));
        } else if ("desc".equalsIgnoreCase(sortByName)) {
            venues.sort(Comparator.comparing(Venue::getName, String.CASE_INSENSITIVE_ORDER).reversed());
        }

        // send data to view
        model.addAttribute("venues", venues);
        model.addAttribute("sortByName", sortByName);
        model.addAttribute("sortByCounty", sortByCounty);
        model.addAttribute("sortByCity", sortByCity);
        model.addAttribute("counties", countyService.getAllCountyNames());

        List<String> cityNames = (sortByCounty != null && !sortByCounty.isBlank())
                ? cityService.getCityNamesByCountyName(sortByCounty)
                : cityService.getAllCityNames();

        model.addAttribute("cities", cityNames);

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

        } catch (Exception e) {
            model.addAttribute("savingError", "Unable to save venue: " + e.getMessage());
            model.addAttribute("counties", countyService.getAllCounties());

            return "add-venue-form";
        }

        return "redirect:/venues";
    }
}
