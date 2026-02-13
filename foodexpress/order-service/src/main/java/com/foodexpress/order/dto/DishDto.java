package com.foodexpress.order.dto;

import java.math.BigDecimal;

public class DishDto {
    public Long id;
    public String name;
    public String description;
    public BigDecimal price;
    public String category;
    public boolean available;
    public String allergens;
}
