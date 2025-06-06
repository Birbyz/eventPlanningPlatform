package com.example.eventplanner.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventplanner.database.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>{
    // data manipulation methods - filtering, sorting etc.
}
