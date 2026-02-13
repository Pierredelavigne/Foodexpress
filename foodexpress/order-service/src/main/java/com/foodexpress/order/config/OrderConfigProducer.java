package com.foodexpress.order.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;

@ApplicationScoped
public class OrderConfigProducer {

    @ConfigProperty(name = "foodexpress.order.min-amount")
    BigDecimal minAmount;

    @ConfigProperty(name = "foodexpress.order.max-delivery-radius-km")
    int maxRadiusKm;

    @Produces
    public OrderConfig orderConfig() {
        return new OrderConfig(minAmount, maxRadiusKm);
    }
}
