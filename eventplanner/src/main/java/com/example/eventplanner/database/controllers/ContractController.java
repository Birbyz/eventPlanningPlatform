package com.example.eventplanner.database.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventplanner.database.services.ContractService;

@RestController
@RequestMapping("/contracts")
public class ContractController {
    
    @Autowired
    private ContractService contractService;
}
