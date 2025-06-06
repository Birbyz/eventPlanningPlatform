package com.example.eventplanner.database.entities;

import java.time.LocalDateTime;
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

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "location", nullable = false)
    private String location;

    // FOREIGN KEYS
    @ManyToOne
    @JoinColumn(name = "organizer_id", referencedColumnName = "id", nullable = false)
    private Organizer organizer;

    @OneToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id", nullable = false)
    private Venue venue;

    @OneToMany(mappedBy = "event")
    private Set<Contract> contracts;

    @ManyToMany
    @JoinTable(
        name = "events_and_guests",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    private Set<Guest> guests;
}
