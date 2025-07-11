package com.example.eventplanner.database.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.example.eventplanner.database.entities.Event;
import com.example.eventplanner.database.entities.Guest;
import com.example.eventplanner.database.entities.Organizer;
import com.example.eventplanner.database.entities.Service;
import com.example.eventplanner.database.entities.Vendor;
import com.example.eventplanner.database.services.CityService;
import com.example.eventplanner.database.services.ContractService;
import com.example.eventplanner.database.services.CountyService;
import com.example.eventplanner.database.services.EventService;
import com.example.eventplanner.database.services.OrganizerService;
import com.example.eventplanner.database.services.ServiceService;
import com.example.eventplanner.database.services.VenueService;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private CountyService countyService;

    @Autowired
    private CityService cityService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private OrganizerService organizerService;

    @Autowired
    private ContractService contractService;

    @GetMapping("")
    public String showEventsScreen(Model model) {
        model.addAttribute("events", eventService.getAllEvents());

        return "events";
    }

    // ADD EVENT
    @GetMapping("/add")
    public String showAddEventForm(Model model) {
        Event event = new Event();
        event.setGuests(new ArrayList<>(List.of(new Guest())));

        model.addAttribute("event", event);
        System.out.println("Guests in GET form: " + event.getGuests().size());

        loadFormData(model);

        return "add-event-form";
    }

    @PostMapping("/add")
    public String addEvent(
            @Valid @ModelAttribute("event") Event event,
            BindingResult result,
            @RequestParam(required = false) List<Long> selectedServiceIds,
            Model model,
            RedirectAttributes redirectAttributes,
            Principal principal // logged user interface
    ) {
        if (result.hasErrors()) {
            // System.out.println("VALIDATION ERRORS: " + result.getAllErrors());
            System.out.println("RESULT HAS ERRORS");

            loadFormData(model);
            model.addAttribute("modalError", "Please correct the errors in the form.");

            return "add-event-form";
        }

        // check if venue has already been used
        if (eventService.isVenueAlreadyTaken(event.getVenue().getId())) {
            model.addAttribute("modalError", "This venue is already taken by another event.");
            loadFormData(model);
            System.err.println("VENUE IS ALREADY TAKEN");
            result.rejectValue("venue.id", "error.venue", "This venue is already taken.");

            return "add-event-form";
        }

        // check the user existence
        String email = principal.getName();
        Organizer organizer = organizerService
                .getOrganizerByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Organizer with email " + email + " not found"));

        Event newEvent = new Event();
        newEvent.setOrganizer(organizer);

        try {
            newEvent.setTitle(event.getTitle());
            newEvent.setDescription(event.getDescription());
            newEvent.setDate(event.getDate());
            newEvent.setVenue(event.getVenue());
            newEvent.setGuests(event.getGuests());
            newEvent.setSelectedServiceIds(event.getSelectedServiceIds());

            eventService.save(newEvent); // save event before adding contracts
            redirectAttributes.addFlashAttribute("success", "Event added successfully.");
        } catch (ConstraintViolationException e) {
            String errorMsg = e.getConstraintViolations().stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.joining(". "));

            redirectAttributes.addFlashAttribute("modalError", errorMsg);

            System.out.println("EVENT ERR");
            return "redirect:/events/add";
        }

        // set items - CONTRACTS
        if (selectedServiceIds == null || selectedServiceIds.isEmpty()) {
            result.rejectValue("contracts", null, "Please select at least one service.");
            loadFormData(model);

            return "add-event-form";
        }

        Map<Vendor, List<Service>> servicesByVendor = new HashMap<>();

        for (Long serviceId : selectedServiceIds) {
            Service service = serviceService
                    .getServiceById(serviceId)
                    .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + serviceId));

            Vendor vendor = service.getVendor();

            servicesByVendor
                    .computeIfAbsent(vendor, v -> new ArrayList<>())
                    .add(service);
        }

        // ADD A CONTRACT FOR EACH VENDOR
        for (Map.Entry<Vendor, List<Service>> entry : servicesByVendor.entrySet()) {
            Vendor vendor = entry.getKey();
            List<Service> vendorServices = entry.getValue();

            double totalPrice = vendorServices
                    .stream()
                    .mapToDouble(Service::getPrice)
                    .sum();

            Contract contract = new Contract();
            contract.setVendor(vendor);
            contract.setOrganizer(organizer);
            contract.setEvent(newEvent);
            contract.setSignedAt(LocalDateTime.now());
            contract.setTotalPrice(totalPrice);
            contract.setServices(new ArrayList<>(vendorServices));

            contractService.addContract(contract);
        }

        return "redirect:/events";
    }

    @GetMapping("/view/{id}")
    public String viewEventDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Event> eventRequest = eventService.getEventById(id);
        model.addAttribute("eventId", id);

        if (eventRequest.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Unable to retrieve information about the event");
            System.err.println("INVALID ID: " + id);

            return "redirect:/events";
        }

        Event event = eventRequest.get();
        List<Guest> guests = event.getGuests();
        List<Contract> contracts = contractService.getContractsByEvent(event);

        model.addAttribute("event", event);
        model.addAttribute("guests", guests);
        model.addAttribute("contracts", contracts);

        return "view-event";
    }

    private void loadFormData(Model model) {
        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        model.addAttribute("services", serviceService.getAllServices());
        model.addAttribute("counties", countyService.getAllCounties());
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("venues", venueService.getAllVenues());
    }
}
