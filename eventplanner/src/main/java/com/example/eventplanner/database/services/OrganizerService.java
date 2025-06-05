package com.example.eventplanner.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.repositories.OrganizerRepository;

@Service
public class OrganizerService {
    
    @Autowired
    private OrganizerRepository organizerRepository;

    // write ALL class methods - getAll, save, edit, delete ETC.
}
