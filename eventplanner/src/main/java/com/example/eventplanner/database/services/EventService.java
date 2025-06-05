package com.example.eventplanner.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
}
