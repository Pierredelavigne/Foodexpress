package com.foodexpress.order.dto;

import jakarta.validation.constraints.NotBlank;

public class OrderStatusUpdateRequest {
    @NotBlank
    public String status;
}
