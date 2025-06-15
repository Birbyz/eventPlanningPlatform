package com.example.eventplanner.database.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventplanner.database.entities.Contract;
import com.example.eventplanner.database.entities.Service;
import com.example.eventplanner.database.entities.Vendor;
import com.example.eventplanner.database.services.ContractService;
import com.example.eventplanner.database.services.ServiceService;
import com.example.eventplanner.database.services.VendorService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/services")
public class SeviceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private ContractService contractService;

    private static final List<String> SERVICE_TYPES = List.of(
            "Catering", "BBQ", "Candy Bar", "Photo Booth", "Live Music", "Decor", "Lighting");

    // View ALL
    @GetMapping("")
    public String showAllServices(Model model) {
        model.addAttribute("services", serviceService.getAllServices());
        return "services";
    }

    // ADD
    @GetMapping("/add")
    public String showAddVendorServiceForm(@RequestParam("vendorId") Long vendorId, Model model) {
        model.addAttribute("service", new Service());

        System.out.println(vendorId);

        // service types
        model.addAttribute("serviceTypes", SERVICE_TYPES);
        model.addAttribute("vendorId", vendorId);

        return "add-vendor-service-form";
    }

    @PostMapping("/add")
    public String addService(@RequestParam("vendorId") Long vendorId, @Valid @ModelAttribute Service service,
            BindingResult result, Model model) {
        System.out.println(vendorId + " entered form");

        if (vendorId == null) {
            model.addAttribute("savingError", "Vendor ID is missing. Cannot create service.");
            model.addAttribute("serviceTypes", SERVICE_TYPES);

            return "add-vendor-service-form";
        }

        if (result.hasErrors()) {
            System.out.println("VALIDATION ERRORS: " + result.getAllErrors());
            model.addAttribute("vendorId", vendorId);
            model.addAttribute("serviceTypes", SERVICE_TYPES);

            return "add-vendor-service-form";
        }

        try {
            Vendor vendor = vendorService.getVendorById(vendorId);

            Service newService = new Service();
            newService.setPrice(service.getPrice());
            newService.setType(service.getType());
            newService.setVendor(vendor);

            serviceService.addService(newService);
        } catch (Exception e) {
            model.addAttribute("savingError", "Unable to save service: " + e.getMessage());
            model.addAttribute("serviceTypes", SERVICE_TYPES);
            model.addAttribute("vendorId", vendorId);

            return "add-vendor-service-form";
        }

        return "redirect:/events";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Service service = serviceService.getServiceById(id).orElse(null);
        if (service == null) {
            redirectAttributes.addFlashAttribute("error", "Service not found");

            return "redirect:/services";
        }
        System.out.println("service found");

        if (!serviceService.canDeleteService(id)) {
            System.out.println("SERVICE STATUS: ID = " + id);
            redirectAttributes.addFlashAttribute("error", "Service cannot be deleted because it's used in an upcoming or ongoing event.");
            return "redirect:/services";
        }

        serviceService.deleteServiceById(id);
        
        redirectAttributes.addFlashAttribute("success", "Service deleted successfully.");
        return "redirect:/services";
    }



}
