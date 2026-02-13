package com.foodexpress.restaurant.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class DishRequest {

    @NotBlank
    public String name;

    public String description;

    @NotNull
    @Positive
    public BigDecimal price;

    @NotBlank
    public String category; // STARTER, MAIN, DESSERT, DRINK

    public Boolean available = true;

    public String allergens;
}
