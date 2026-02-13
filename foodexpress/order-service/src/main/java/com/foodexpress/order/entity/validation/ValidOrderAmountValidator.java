package com.foodexpress.order.entity.validation;

import com.foodexpress.order.config.OrderConfig;
import com.foodexpress.order.entity.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

@ApplicationScoped
public class ValidOrderAmountValidator implements ConstraintValidator<ValidOrderAmount, Order> {

    @Inject
    OrderConfig config;

    @Override
    public boolean isValid(Order order, ConstraintValidatorContext ctx) {
        if (order == null || order.items == null) return true;

        BigDecimal sum = order.items.stream()
                .filter(i -> i.unitPrice != null)
                .map(i -> i.unitPrice.multiply(BigDecimal.valueOf(i.quantity)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boolean okSum = order.totalAmount != null && order.totalAmount.compareTo(sum) == 0;
        boolean okMin = order.totalAmount != null && order.totalAmount.compareTo(config.minAmount) >= 0;

        if (!okSum || !okMin) {
            ctx.disableDefaultConstraintViolation();
            if (!okSum) {
                ctx.buildConstraintViolationWithTemplate("totalAmount does not match items sum")
                        .addPropertyNode("totalAmount").addConstraintViolation();
            }
            if (!okMin) {
                ctx.buildConstraintViolationWithTemplate("totalAmount is below minimum (" + config.minAmount + ")")
                        .addPropertyNode("totalAmount").addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}
