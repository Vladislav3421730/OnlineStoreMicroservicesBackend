package com.example.market.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OrderStatusValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderStatus {
    String message() default "Status must be in ('принят','в обработке','в пути','доставлен')";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}