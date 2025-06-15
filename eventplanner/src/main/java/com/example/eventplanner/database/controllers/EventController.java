package com.example.eventplanner.database.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.example.eventplanner.database.services.GuestService;
import com.example.eventplanner.database.services.OrganizerService;
import com.example.eventplanner.database.services.ServiceService;
import com.example.eventplanner.database.services.VenueService;

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

    @Autowired
    private GuestService guestService;

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

        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        model.addAttribute("services", serviceService.getAllServices());
        model.addAttribute("counties", countyService.getAllCounties());
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("venues", venueService.getAllVenues());

        return "add-event-form";
    }

    @PostMapping("/add")
    public String addEvent(
            @Valid @ModelAttribute("event") Event event,
            @RequestParam(required = false) List<Long> selectedServiceIds,
            BindingResult result,
            Model model,
            Principal principal // logged user interface
    ) throws Exception {
        if (result.hasErrors()) {
            System.out.println("VALIDATION ERRORS: " + result.getAllErrors());

            model.addAttribute("minDate", LocalDate.now().plusDays(1));
            model.addAttribute("services", serviceService.getAllServices());
            model.addAttribute("counties", countyService.getAllCounties());
            model.addAttribute("cities", cityService.getAllCities());
            model.addAttribute("venues", venueService.getAllVenues());

            return "add-event-form";
        }

        // check the user existence
        String email = principal.getName();
        Organizer organizer = organizerService
                .getOrganizerByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Organizer with email " + email + " not found"));

        Event newEvent = new Event();
        newEvent.setOrganizer(organizer);
        newEvent.setTitle(event.getTitle());
        newEvent.setDescription(event.getDescription());
        newEvent.setDate(event.getDate());
        newEvent.setVenue(event.getVenue());
        newEvent.setGuests(event.getGuests());
        newEvent.setSelectedServiceIds(event.getSelectedServiceIds());

        eventService.save(newEvent); // save event before adding contracts

        // set items - CONTRACTS
        if (selectedServiceIds == null || selectedServiceIds.isEmpty()) {
            result.rejectValue("contracts", null, "Please select at least one service.");
            model.addAttribute("minDate", LocalDate.now().plusDays(1));
            model.addAttribute("services", serviceService.getAllServices());
            model.addAttribute("counties", countyService.getAllCounties());
            model.addAttribute("cities", cityService.getAllCities());
            model.addAttribute("venues", venueService.getAllVenues());

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
}
