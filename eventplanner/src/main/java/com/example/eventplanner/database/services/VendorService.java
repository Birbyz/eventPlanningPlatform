package com.example.eventplanner.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Vendor;
import com.example.eventplanner.database.repositories.VendorRepository;

@Service
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public Vendor addVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }
}
