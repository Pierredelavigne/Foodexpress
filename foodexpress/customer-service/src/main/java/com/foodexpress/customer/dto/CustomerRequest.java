package com.foodexpress.customer.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

    @NotBlank
    @Size(min = 2, max = 50)
    public String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    public String lastName;

    @NotBlank
    @Email
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

    public Boolean active;
}
