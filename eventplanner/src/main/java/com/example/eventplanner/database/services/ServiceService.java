package com.example.eventplanner.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.eventplanner.database.entities.Service;
import com.example.eventplanner.database.repositories.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceService {
    
    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service addService(Service service) {
        return serviceRepository.save(service);
    }
}
