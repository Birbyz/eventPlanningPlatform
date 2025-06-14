package com.example.eventplanner.database.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 30, message = "The title must be between 3 and 30 characters.")
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 100, message = "The title must have no more than 100 characters.")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Date must not be empty.")
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    // FOREIGN KEYS
    @ManyToOne
    @JoinColumn(name = "organizer_id", referencedColumnName = "id", nullable = false)
    private Organizer organizer;

    @NotNull(message = "Choose a venue based on the selected county and city")
    @OneToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id", nullable = false)
    private Venue venue;

    @OneToMany(mappedBy = "event")
    private Set<Contract> contracts = new HashSet<>();

    @Valid
    @ManyToMany
    @JoinTable(
        name = "events_and_guests",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    @Size(min = 1, message = "At least one guest is required")
    private List<Guest> guests = new ArrayList<>();
}
