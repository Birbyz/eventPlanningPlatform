package com.example.eventplanner.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.repositories.VendorRepository;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;
}
