package com.foodexpress.restaurant.dto;

import com.foodexpress.restaurant.entity.Restaurant;

public class RestaurantResponse {
    public Restaurant restaurant;
    public String menu; // rendu formaté

    public RestaurantResponse(Restaurant restaurant, String menu) {
        this.restaurant = restaurant;
        this.menu = menu;
    }
}
