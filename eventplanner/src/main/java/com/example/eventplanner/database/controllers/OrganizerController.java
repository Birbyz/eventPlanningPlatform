package com.example.eventplanner.database.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import com.example.eventplanner.database.entities.Organizer;
import com.example.eventplanner.database.services.OrganizerService;

@Controller
@RequestMapping("/organizers")
public class OrganizerController {
    
    @Autowired
    private OrganizerService organizerService;

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" 
        + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"; 

    @GetMapping("/register")
    public String showRegisterForm(WebRequest request, Model model) {
        model.addAttribute("organizer", new Organizer());

        return "register";
    }

    @PostMapping(value = "/register")
    public void register(@ModelAttribute Organizer organizer) throws Exception {
        if (!isEmailValid(organizer.getEmail())) {
            throw new Exception("Invalid email");
        } 
        System.err.println("EROARE VALIDARE");

        if (emailExists(organizer.getEmail())) {
            throw new Exception("There is already an account with this email address");
        }

        // if user data is fine, add it in the DB
        Organizer newOrganizer = new Organizer();
        newOrganizer.setName(organizer.getName());
        newOrganizer.setSurname(organizer.getSurname());
        newOrganizer.setEmail(organizer.getEmail());
        newOrganizer.setPhone(organizer.getPhone());
        newOrganizer.setPassword(organizer.getPassword());

        try {
            organizerService.addOrganizer(newOrganizer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register", e);
        }
    }
    
    private boolean emailExists(String email) {
        return organizerService.existsByEmail(email);
    }

    private boolean isEmailValid(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
