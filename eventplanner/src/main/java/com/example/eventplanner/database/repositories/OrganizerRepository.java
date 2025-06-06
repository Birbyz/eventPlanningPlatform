package com.example.eventplanner.database.repositories;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventplanner.database.entities.Organizer;

@Repository // receives a param - Class + the ID type
public interface OrganizerRepository extends JpaRepository<Organizer, Long>{
    // finders, methods etc.

    Optional<Organizer> findByEmail(String email);
}
