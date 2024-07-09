package com.ecom.webshop.store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=ImageNameValidator.class)
public @interface ImageNameValid{
    //Error message
    String message() default "Invalid Image name !!";

    //represent group of constraints
    Class<?>[] groups() default { };

    //additional information about annotation
    Class<? extends Payload>[] payload() default { };

}
