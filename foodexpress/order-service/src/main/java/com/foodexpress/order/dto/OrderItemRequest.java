package com.foodexpress.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderItemRequest {
    @NotNull
    public Long dishId;

    @Min(1)
    public int quantity;
}
