package com.example.eventplanner.database.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventplanner.database.repositories.ContractRepository;

@Service
public class ContractService {
    
    @Autowired
    private ContractRepository contractRepository;
}
