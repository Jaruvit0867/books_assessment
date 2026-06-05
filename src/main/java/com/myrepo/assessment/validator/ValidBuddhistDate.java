package com.myrepo.assessment.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Custom annotation for validating Buddhist date.
@Constraint(validatedBy = BuddhistDateValidator.class)

//Tell the annotation to be used on fields.
@Target(ElementType.FIELD)

//Tell the annotation to be retained at runtime.
@Retention(RetentionPolicy.RUNTIME)

//@interface -> for the use of the custom annotation.
public @interface ValidBuddhistDate {

    // Error message when validation fails.
    String message() default "publishedDate must be a valid date with year > 1000 and <= current year (in Buddhist calendar)";

    // Allows grouping validations.
    Class<?>[] groups() default {};

    // Allows attaching extra metadata.
    Class<? extends Payload>[] payload() default {};
}
