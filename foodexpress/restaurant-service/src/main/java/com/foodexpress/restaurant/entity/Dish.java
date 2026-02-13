package com.foodexpress.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "dishes")
public class Dish extends PanacheEntity {

    @NotBlank
    public String name;

    public String description;

    @NotNull
    @Positive
    public BigDecimal price;

    @NotBlank
    public String category; // STARTER, MAIN, DESSERT, DRINK

    public boolean available = true;

    public String allergens;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnore // ✅ empêche la boucle JSON + lazy exception
    public Restaurant restaurant;
}
