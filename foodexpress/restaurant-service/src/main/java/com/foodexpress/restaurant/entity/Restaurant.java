package com.foodexpress.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant extends PanacheEntity {

    @NotBlank
    @Size(min = 2, max = 100)
    public String name;

    @NotBlank
    public String cuisine;

    @NotBlank
    public String address;

    @NotBlank
    public String city;

    @Pattern(regexp = "^(\\+33|0)[1-9](\\d{2}){4}$", message = "invalid phone format")
    public String phone;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    public Double rating;

    @NotNull
    public LocalTime openingTime;

    @NotNull
    public LocalTime closingTime;

    public boolean active = true;

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore // ✅ empêche lazy loading lors de la sérialisation JSON
    public List<Dish> dishes = new ArrayList<>();

    @AssertTrue(message = "openingTime must be before closingTime")
    public boolean isOpeningBeforeClosing() {
        if (openingTime == null || closingTime == null) return true;
        return openingTime.isBefore(closingTime);
    }

    // Requêtes Panache utiles
    public static List<Restaurant> findByCuisine(String cuisine) {
        return list("cuisine = ?1 and active = true", cuisine);
    }

    public static List<Restaurant> findByCuisineInCity(String cuisine, String city) {
        return list("cuisine = ?1 and city = ?2 and active = true", cuisine, city);
    }

    public static List<Restaurant> findWithMinRating(double min) {
        return list("rating >= ?1 and active = true", min);
    }
}
