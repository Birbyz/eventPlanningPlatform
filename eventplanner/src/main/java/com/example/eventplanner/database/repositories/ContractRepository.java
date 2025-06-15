package com.example.eventplanner.database.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventplanner.database.entities.Contract;
import com.example.eventplanner.database.entities.Event;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByEvent(Event event);
}
