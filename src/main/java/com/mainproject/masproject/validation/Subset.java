package com.mainproject.masproject.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SubsetValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Subset {
    String message() default "must be a subset of allowed values";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] anyOf(); // dozwolone wartości
}
