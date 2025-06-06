package com.example.eventplanner.database.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventplanner.database.services.OrganizerService;

@Controller
@RequestMapping("/organizers")
public class OrganizerController {
    
    @Autowired
    private OrganizerService organizerService;
}
