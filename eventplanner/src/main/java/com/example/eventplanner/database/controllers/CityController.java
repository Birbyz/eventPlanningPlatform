package com.example.eventplanner.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.services.CityService;

@RestController
public class CityController {
    @Autowired
    private CityService cityService;
}
