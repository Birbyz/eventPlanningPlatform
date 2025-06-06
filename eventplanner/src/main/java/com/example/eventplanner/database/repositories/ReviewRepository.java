package com.example.eventplanner.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.eventplanner.database.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
    
}
