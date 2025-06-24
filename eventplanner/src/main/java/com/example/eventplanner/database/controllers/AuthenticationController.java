package com.example.eventplanner.database.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventplanner.database.entities.Organizer;
import com.example.eventplanner.database.services.OrganizerService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // LOGIN
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // REGISTER
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("organizer", new Organizer());

        return "register";
    }

    @PostMapping(value = "/register")
    public String register(
            @Valid @ModelAttribute("organizer") Organizer organizer,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (emailExists(organizer.getEmail())) {
            redirectAttributes.addFlashAttribute("modalError", "This email has already been registered.");
            redirectAttributes.addFlashAttribute("organizer", organizer);
            return "redirect:/register";
        }
        
        if (result.hasErrors()) {
            System.out.println("RESULT ERRORS");

            // String errorMsg = result.getFieldErrors().stream()
            // .map(DefaultMessageSourceResolvable::getDefaultMessage)
            // .collect(Collectors.joining(". "));

            // redirectAttributes.addFlashAttribute("modalError", errorMsg);

            return "register";
        }

        // if user data is fine, add it in the DB
        Organizer newOrganizer = new Organizer();
        newOrganizer.setName(organizer.getName());
        newOrganizer.setSurname(organizer.getSurname());
        newOrganizer.setEmail(organizer.getEmail());
        newOrganizer.setPhone(organizer.getPhone());
        newOrganizer.setPassword(passwordEncoder.encode(organizer.getPassword()));

        try {
            organizerService.addOrganizer(newOrganizer);
            // redirectAttributes.addFlashAttribute("modalSucces", "Registration
            // complete!.");
        } catch (ConstraintViolationException e) {
            System.err.println("EROARE VALIDARE");

            String errorMsg = e.getConstraintViolations().stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.joining(". "));

            // redirectAttributes.addFlashAttribute("modalError", errorMsg);
            return "register";
        }

        return "redirect:/login";
    }

    private boolean emailExists(String email) {
        return organizerService.existsByEmail(email);
    }
}
