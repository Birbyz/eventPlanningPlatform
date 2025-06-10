package com.example.eventplanner.database.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.entities.County;
import com.example.eventplanner.database.repositories.CountyRepository;

@Service
public class CountyService {
    @Autowired
    private CountyRepository countyRepository;

    public List<County> getAllCounties() {
        return countyRepository.findAll();
    }
}
