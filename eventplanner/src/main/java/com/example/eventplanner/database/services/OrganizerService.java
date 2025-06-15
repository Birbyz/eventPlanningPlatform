package com.example.eventplanner.database.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Organizer;
import com.example.eventplanner.database.repositories.OrganizerRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class OrganizerService {
    
    @Autowired
    private OrganizerRepository organizerRepository;

    // DATABASE OPERATIONS
    public Organizer addOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
    }

    public Optional<Organizer> getOrganizerByEmail(String email) {
        return organizerRepository.findByEmail(email);
    }

    public Boolean existsByEmail(String email) {
        return organizerRepository.findByEmail(email).isPresent() ;
    }

    public Optional<Organizer> getOrganizerById(Long id) {
        return organizerRepository.findById(id);
    }
}
