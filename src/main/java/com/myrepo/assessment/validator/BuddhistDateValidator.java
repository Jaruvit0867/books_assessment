package com.myrepo.assessment.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Year;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BuddhistDateValidator implements ConstraintValidator<ValidBuddhistDate, String> {

    private static final DateTimeFormatter BUDDHIST_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            DateTimeFormatter formatter = BUDDHIST_FORMATTER
                    .withChronology(ThaiBuddhistChronology.INSTANCE);
            LocalDate gregorianDate = LocalDate.from(formatter.parse(value));
            int gregorianYear = gregorianDate.getYear();

            if (gregorianYear <= 1000) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("publishedDate year must be greater than 1000 (Gregorian)")
                        .addConstraintViolation();
                return false;
            }

            if (gregorianYear > Year.now().getValue()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("publishedDate year must not be in the future")
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (DateTimeParseException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("publishedDate must be a valid date in format dd-MM-yyyy (Buddhist calendar)")
                    .addConstraintViolation();
            return false;
        }
    }
}
