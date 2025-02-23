package com.example.market.annotation;


import com.example.market.model.enums.Status;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class OrderStatusValidator implements ConstraintValidator<OrderStatus, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(Status.values())
                .map(Status::getDisplayName)
                .anyMatch(status -> status.equals(value));
    }
}
