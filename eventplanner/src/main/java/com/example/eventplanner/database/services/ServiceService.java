package com.example.eventplanner.database.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.eventplanner.database.entities.Service;
import com.example.eventplanner.database.repositories.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service addService(Service service) {
        return serviceRepository.save(service);
    }

    public Optional<Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public void deleteServiceById(Long id) {
        serviceRepository.deleteById(id);
    }

    /*
     * A service cannot be deleted if it is used in the contract of an upcoming
     * event.
     */
    public Boolean canDeleteService(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + serviceId));

        return service.getContracts()
                .stream()
                .noneMatch(contract -> contract.getEvent() != null &&
                        !contract.getEvent().getDate().isBefore(LocalDate.now()));
    }
}
