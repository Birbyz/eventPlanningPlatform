package com.example.eventplanner.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Event;
import com.example.eventplanner.database.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
