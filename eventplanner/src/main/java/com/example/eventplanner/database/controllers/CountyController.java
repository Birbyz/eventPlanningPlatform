package com.example.eventplanner.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.eventplanner.database.services.CountyService;

@Controller
public class CountyController {
    @Autowired
    private CountyService countyService;
}
