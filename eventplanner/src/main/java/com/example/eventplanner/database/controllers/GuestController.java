package com.example.eventplanner.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.eventplanner.database.services.GuestService;

@Controller
@RequestMapping("/guests")
public class GuestController {
    
    @Autowired
    private GuestService guestService;
}
