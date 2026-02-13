package com.foodexpress.order.entity;

import com.foodexpress.order.entity.validation.ValidOrderAmount;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@ValidOrderAmount
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotNull
    public Long customerId;

    @NotNull
    public Long restaurantId;

    @Enumerated(EnumType.STRING)
    public OrderStatus status = OrderStatus.CREATED;

    @NotNull
    public BigDecimal totalAmount = BigDecimal.ZERO;

    @NotBlank
    public String deliveryAddress;

    public LocalDateTime createdAt = LocalDateTime.now();
    public LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotEmpty
    @Valid
    public List<OrderItem> items = new ArrayList<>();
}
