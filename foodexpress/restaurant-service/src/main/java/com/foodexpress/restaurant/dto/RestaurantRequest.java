package com.foodexpress.restaurant.dto;

import jakarta.validation.constraints.*;

public class RestaurantRequest {

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
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    public Double rating;

    @NotBlank
    public String openingTime; // "09:00"

    @NotBlank
    public String closingTime; // "23:00"

    public Boolean active = true;
}
