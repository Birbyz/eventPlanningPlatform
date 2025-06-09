package com.example.eventplanner.database.controllers;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.eventplanner.database.entities.Vendor;
import com.example.eventplanner.database.services.VendorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/vendors")
public class VendorController implements WebMvcConfigurer{
    @Autowired
    private VendorService vendorService;

    // VENDORS SCREEN
    @GetMapping()
    public String showVendorsScreen(Model model) {
        model.addAttribute("vendors", vendorService.getAllVendors());
        return "vendors";
    }

    // ADD
    @GetMapping("/add")
    public String showAddVendorForm(Model model) {
        model.addAttribute("vendor", new Vendor());
        return "add-vendor-form";
    }

    @PostMapping(value = "/add")
    public String addVendor(@Valid @ModelAttribute Vendor vendor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //System.out.println("VALIDATION ERRORS: " + result.getAllErrors() + result + vendor.toString());
            return "add-vendor-form";
        } 

        Vendor newVendor = new Vendor();
        newVendor.setName(vendor.getName());
        newVendor.setEmail(vendor.getEmail());
        newVendor.setPhone(vendor.getPhone());
        
        vendorService.addVendor(newVendor);
        return "redirect:/vendors";
    }
}
