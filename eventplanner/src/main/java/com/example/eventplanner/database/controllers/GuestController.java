package com.example.eventplanner.database.controllers;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventplanner.database.entities.Event;
import com.example.eventplanner.database.entities.Guest;
import com.example.eventplanner.database.services.EventService;
import com.example.eventplanner.database.services.GuestService;

import jakarta.validation.ConstraintViolationException;

@Controller
@RequestMapping("/guests")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @Autowired
    private EventService eventService;

    @PostMapping("/add")
    public String addGuest(
            @RequestParam(required = false) Long eventId,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes) {
        if (guestService.existsByPhone(phone)) {
            redirectAttributes.addFlashAttribute("modalError", "Phone number already exists.");
            redirectAttributes.addFlashAttribute("reopenModal", true);
            redirectAttributes.addFlashAttribute("guestId", "");
            redirectAttributes.addFlashAttribute("guestName", name);
            redirectAttributes.addFlashAttribute("guestSurname", surname);
            redirectAttributes.addFlashAttribute("guestPhone", phone);
            return "redirect:/events/view/" + eventId;
        }

        Guest newGuest = new Guest();
        newGuest.setName(name);
        newGuest.setSurname(surname);
        newGuest.setPhone(phone);

        System.out.println("EVENT ID ENTERED IN FORM: " + eventId);
        Optional<Event> optionalEvent = eventService.getEventById(eventId);
        if (optionalEvent.isEmpty()) {
            System.err.println("EVENT IS EMPTY");
            redirectAttributes.addFlashAttribute("modalError", "Unable to retrieve event data");

            return "redirect:/events/view/" + eventId;
        }

        Event event = optionalEvent.get();
        newGuest.getEvents().add(event);

        try {
            event.getGuests().add(newGuest);
            guestService.addGuest(newGuest);
            redirectAttributes.addFlashAttribute("success", "Guest added successfully.");
        } catch (ConstraintViolationException e) {
            String errorMsg = e.getConstraintViolations().stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.joining(". "));

            redirectAttributes.addFlashAttribute("modalError", errorMsg);
            redirectAttributes.addFlashAttribute("reopenModal", true);
            redirectAttributes.addFlashAttribute("guestName", name);
            redirectAttributes.addFlashAttribute("guestSurname", surname);
            redirectAttributes.addFlashAttribute("guestPhone", phone);
        }

        return "redirect:/events/view/" + eventId;
    }

    @PostMapping("/edit/{id}")
    public String updateGuest(
            @PathVariable Long id,
            @RequestParam Long eventId,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String phone,
            RedirectAttributes redirectAttributes) {
        Optional<Guest> optionalGuest = guestService.getById(id);
        if (optionalGuest.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Guest not found.");
            return "redirect:/events";
        }

        Guest guest = optionalGuest.get();

        // check phone duplicate
        if (guestService.existsByPhoneAndIdNot(phone, id)) {
            redirectAttributes.addFlashAttribute("modalError", "Phone number already exists for another guest.");
            redirectAttributes.addFlashAttribute("reopenModal", true);
            redirectAttributes.addFlashAttribute("guestId", id);
            redirectAttributes.addFlashAttribute("guestName", name);
            redirectAttributes.addFlashAttribute("guestSurname", surname);
            redirectAttributes.addFlashAttribute("guestPhone", phone);
            System.err.println("DUPLICATE PHONE");

            return "redirect:/events/view/" + eventId;
        }

        guest.setName(name);
        guest.setSurname(surname);
        guest.setPhone(phone);

        try {
            guestService.addGuest(guest);
            redirectAttributes.addFlashAttribute("modalSucces", "Guest updated successfully.");
        } catch (TransactionSystemException ex) {
            Throwable cause = ex.getRootCause(); // Asta e cheia
            if (cause instanceof ConstraintViolationException) {
                String errorMsg = ((ConstraintViolationException) cause).getConstraintViolations().stream()
                        .map(v -> v.getMessage())
                        .collect(Collectors.joining(". "));

                redirectAttributes.addFlashAttribute("modalError", errorMsg);
            } else {
                redirectAttributes.addFlashAttribute("modalError", "Unexpected error while saving guest.");
                System.err.println("ERROR: " + cause.getMessage()); // log real
            }

            redirectAttributes.addFlashAttribute("reopenModal", true);
            redirectAttributes.addFlashAttribute("guestId", guest.getId());
            redirectAttributes.addFlashAttribute("guestName", guest.getName());
            redirectAttributes.addFlashAttribute("guestSurname", guest.getSurname());
            redirectAttributes.addFlashAttribute("guestPhone", guest.getPhone());
        }

        return "redirect:/events/view/" + eventId;
    }

    @GetMapping("/delete/{id}")
    public String deleteGuest(
            @PathVariable("id") Long guestId,
            @RequestParam("eventId") Long eventId,
            RedirectAttributes redirectAttributes) {
        Optional<Guest> guestOptional = guestService.getById(guestId);
        Optional<Event> eventOptional = eventService.getEventById(eventId);

        if (guestOptional.isEmpty() || eventOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("modalError", "Failed to delete guest.");

            return "redirect:/events/view/" + eventId;
        }

        Guest guest = guestOptional.get();
        Event event = eventOptional.get();

        // check if the current guest is the last one of the selected event
        if (event.getGuests().size() == 1 && event.getGuests().contains(guest)) {
            redirectAttributes.addFlashAttribute("modalError", "An event must have at least one guest.");
            return "redirect:/events/view/" + eventId;
        }

        // remove the many to many relationship
        guest.getEvents().remove(event);
        event.getGuests().remove(guest);

        if (guest.getEvents().isEmpty()) {
            guestService.deleteGuest(guest);
            redirectAttributes.addFlashAttribute("success", "Guest removed and deleted.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Guest removed from event.");
        }

        return "redirect:/events/view/" + eventId;
    }
}
