package com.example.eventplanner.database.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventplanner.database.entities.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    
}
