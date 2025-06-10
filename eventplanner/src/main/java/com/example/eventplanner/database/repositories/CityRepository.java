package com.example.eventplanner.database.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventplanner.database.entities.City;
import com.example.eventplanner.database.entities.County;

@Repository
public interface CityRepository extends JpaRepository<City, Long>{
    List<City> findByCountyId(Long id);
}
