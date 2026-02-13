package com.foodexpress.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderCreateRequest {

    @NotNull
    public Long customerId;

    @NotNull
    public Long restaurantId;

    @NotEmpty
    @Valid
    public List<OrderItemRequest> items;

    @NotBlank
    public String deliveryAddress;
}
