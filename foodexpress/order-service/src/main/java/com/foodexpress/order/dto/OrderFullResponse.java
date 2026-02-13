package com.foodexpress.order.dto;

import com.foodexpress.order.entity.Order;

public class OrderFullResponse {
    public Order order;
    public CustomerDto customer;
    public RestaurantDto restaurant;
    public DeliveryDto delivery;

    public OrderFullResponse() {}

    public OrderFullResponse(Order order, CustomerDto customer, RestaurantDto restaurant, DeliveryDto delivery) {
        this.order = order;
        this.customer = customer;
        this.restaurant = restaurant;
        this.delivery = delivery;
    }
}
