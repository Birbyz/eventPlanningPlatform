package com.example.eventplanner.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.City;
import com.example.eventplanner.database.entities.County;
import com.example.eventplanner.database.repositories.CityRepository;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> getCitiesByCountyId(Long id) {
        return cityRepository.findByCountyId(id);
    }
}
