package com.example.eventplanner.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventplanner.database.entities.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long>{
    // methods which imply sorting, data manipulation (not operations)
}
