package com.example.eventplanner.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person {
    @NotNull
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 3, max = 15, message = "Surname must be between 3 and 15 characters")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Pattern(regexp = "^07\\d{8}$", message = "Phone number must start with 07 and be exactly 10 digits")
    @Column(name = "phone", nullable = false)
    private String phone;
}
