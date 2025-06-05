package com.example.eventplanner.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.repositories.ServiceRepository;

@Service
public class ServiceService {
    
    @Autowired
    private ServiceRepository serviceRepository;
}
