package com.foodexpress.order.entity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidOrderAmountValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderAmount {
    String message() default "Invalid totalAmount";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
