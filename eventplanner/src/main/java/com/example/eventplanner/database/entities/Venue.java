package com.example.eventplanner.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "venue")
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Pattern(regexp = "^[\\p{L} .,'\\-]{3,30}$", message = "Name must be between 3 and 30 characters and can contain only letters, spaces, and punctuation (.,'-)")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 3, max = 30, message = "Address must be between 3 and 30 characters")
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties({"venues", "county"})
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @JsonIgnore
    @OneToOne(mappedBy = "venue")
    private Event event;

    @Transient
    private Long countyId;

}
