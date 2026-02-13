package com.foodexpress.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    public Long dishId;

    public String dishName;

    @NotNull
    public BigDecimal unitPrice;

    @Min(1)
    public int quantity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    public Order order;
}
