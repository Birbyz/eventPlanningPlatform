package com.example.eventplanner.database.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.City;
import com.example.eventplanner.database.repositories.CityRepository;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public List<City> getCitiesByCountyId(Long id) {
        return cityRepository.findByCountyId(id);
    }

    public List<String> getAllCityNames() {
        return cityRepository.findAll().stream()
            .map(City::getName)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public List<String> getCityNamesByCountyName(String countyName) {
        return cityRepository.findByCounty_NameIgnoreCase(countyName).stream()
            .map(City::getName)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
}
