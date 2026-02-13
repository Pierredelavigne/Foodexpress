package com.foodexpress.order.dto;

import com.foodexpress.order.entity.Order;

public class OrderResponse {
    public Order order;
    public DeliveryDto delivery;

    public OrderResponse() {}

    public OrderResponse(Order order, DeliveryDto delivery) {
        this.order = order;
        this.delivery = delivery;
    }
}
