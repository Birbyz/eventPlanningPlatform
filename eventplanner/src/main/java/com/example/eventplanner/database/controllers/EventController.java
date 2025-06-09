package com.example.eventplanner.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.entities.Event;
import com.example.eventplanner.database.services.EventService;
import com.example.eventplanner.database.services.ServiceService;


@Controller
@RequestMapping("/events")
public class EventController {
    
    @Autowired
    private EventService eventService;

    @Autowired ServiceService serviceService;

    // ADD EVENT
    @GetMapping("/add")
    public String showAddEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("allServices", serviceService.getAllServices());

        return "add-event-form";
    }

    @PostMapping(value = "/add")
    public void addEvent(@ModelAttribute Event event) throws Exception {
        // TODO: include guests
        // TODO: include vendors + services
        // TODO: add event itself
    }
}
