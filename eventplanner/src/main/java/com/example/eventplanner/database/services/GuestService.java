package com.example.eventplanner.database.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Guest;
import com.example.eventplanner.database.repositories.GuestRepository;

@Service
public class GuestService {
    // work with Lists, entities have sets

    @Autowired
    private GuestRepository guestRepository;

    public Guest save(Guest guest) {
        return guestRepository.save(guest);
    }

    public Optional<Guest> getById(Long id) {
        return guestRepository.findById(id);
    }

    public List<Guest> getAll() {
        return guestRepository.findAll();
    }
}
