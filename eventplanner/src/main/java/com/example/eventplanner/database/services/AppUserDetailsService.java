package com.example.eventplanner.database.services;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.Organizer;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private OrganizerService organizerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Organizer organizer = organizerService.getOrganizerByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
            organizer.getEmail(), 
            organizer.getPassword(), 
            Collections.emptyList()
        );
    }
}
