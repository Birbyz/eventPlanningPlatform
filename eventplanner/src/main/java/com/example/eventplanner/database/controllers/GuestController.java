package com.example.eventplanner.database.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventplanner.database.entities.Event;
import com.example.eventplanner.database.entities.Guest;
import com.example.eventplanner.database.repositories.EventRepository;
import com.example.eventplanner.database.services.EventService;
import com.example.eventplanner.database.services.GuestService;

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
        RedirectAttributes redirectAttributes
    ) {
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
        event.getGuests().add(newGuest);
        
        guestService.addGuest(newGuest);

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
        guestService.addGuest(guest);

        redirectAttributes.addFlashAttribute("modalSucces", "Guest updated successfully.");
        return "redirect:/events/view/" + eventId;
    }
}
