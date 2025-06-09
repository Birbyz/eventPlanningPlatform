package com.example.eventplanner.database.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank (message = "Name is required")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 charactes")
    private String name;

    @Column(name = "email", nullable = false)
    @NotBlank (message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Column(name = "phone", nullable = false)
    @NotBlank (message = "Phone is required")
    @Pattern(regexp = "^07\\d{8}$", message = "Phone must start with 07 and have 10 DIGITS")
    private String phone;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "vendor")
    private Set<Service> services;
}
