package com.example.eventplanner.database.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.entities.Event;
import com.example.eventplanner.database.entities.Guest;
import com.example.eventplanner.database.services.CityService;
import com.example.eventplanner.database.services.CountyService;
import com.example.eventplanner.database.services.EventService;
import com.example.eventplanner.database.services.ServiceService;
import com.example.eventplanner.database.services.VenueService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private CountyService countyService;

    @Autowired
    private CityService cityService;

    @Autowired
    private VenueService venueService;

    // ADD EVENT
    @GetMapping("/add")
    public String showAddEventForm(Model model) {
        System.out.println("SP");
        Event event = new Event();
        event.setGuests(new ArrayList<>(List.of(new Guest())));
        
        model.addAttribute("event", event);
System.out.println("Guests in GET form: " + event.getGuests().size());
        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        model.addAttribute("services", serviceService.getAllServices());
        model.addAttribute("counties", countyService.getAllCounties());
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("venues", venueService.getAllVenues());
        
        return "add-event-form";
    }

    @PostMapping(value = "/add")
    public String addEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, Model model) throws Exception {
        System.out.println("PLM");
        if (result.hasErrors()) {
            System.out.println("VALIDATION ERRORS: " + result.getAllErrors());

            model.addAttribute("minDate", LocalDate.now().plusDays(1));
            model.addAttribute("services", serviceService.getAllServices());
            model.addAttribute("counties", countyService.getAllCounties());
            model.addAttribute("cities", cityService.getAllCities());
            model.addAttribute("venues", venueService.getAllVenues());
            
            return "add-event-form";
        }

        // TODO: include guests
        // TODO: include vendors + services
        // TODO: add event itself
        return "redirect:/events";
    }
}
