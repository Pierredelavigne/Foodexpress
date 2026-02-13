package com.foodexpress.order.config;

import java.math.BigDecimal;

public class OrderConfig {
    public BigDecimal minAmount;
    public int maxDeliveryRadiusKm;

    public OrderConfig(BigDecimal minAmount, int maxDeliveryRadiusKm) {
        this.minAmount = minAmount;
        this.maxDeliveryRadiusKm = maxDeliveryRadiusKm;
    }
}
