package com.foodexpress.customer.entity;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends PanacheEntity {

    @NotBlank
    @Size(min = 2, max = 50)
    public String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    public String lastName;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    public String email;

    @NotBlank
    @Pattern(regexp = "^(\\+33|0)[1-9](\\d{2}){4}$", message = "invalid phone format")
    public String phone;

    @NotBlank
    public String address;

    @NotBlank
    public String city;

    @NotBlank
    @Pattern(regexp = "^\\d{5}$", message = "zipCode must be 5 digits")
    public String zipCode;

    public LocalDateTime createdAt = LocalDateTime.now();

    public boolean active = true;

    public static List<Customer> findActiveByCity(String city) {
        return list("city = ?1 and active = true", city);
    }

    public static Customer findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public static long countByCity(String city) {
        return count("city = ?1", city);
    }
}
