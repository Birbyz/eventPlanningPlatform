package com.example.eventplanner.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Organizer;
import com.example.eventplanner.database.repositories.OrganizerRepository;

@Service
public class AuthenticationService {
    
    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Organizer login(String email, String password) throws Exception {
        Organizer organizer = organizerRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(password, organizer.getPassword())) {
            throw new Exception("invalid credentials");
        }

        return organizer;
    }
}
