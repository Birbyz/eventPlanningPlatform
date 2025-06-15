package com.example.eventplanner.database.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventplanner.database.entities.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>{

}
