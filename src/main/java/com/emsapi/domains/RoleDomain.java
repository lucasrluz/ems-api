package com.emsapi.domains;

import java.util.Set;

import com.emsapi.domains.util.InvalidRoleDomainException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;

public class RoleDomain {
    @NotBlank
    private String name;

    private RoleDomain(@NotBlank String name) {
        this.name = name;
    }

    public static RoleDomain validate(String name) throws InvalidRoleDomainException {
        try {
            RoleDomain roleDomain = new RoleDomain(name);

            validation(roleDomain);

            return roleDomain;
        } catch (ConstraintViolationException exception) {
            String message = exception.getMessage();

            throw new InvalidRoleDomainException(message);
        }
    }

    private static void validation(RoleDomain roleDomain) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<RoleDomain>> constraintViolations = validator.validate(roleDomain);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public String getName() {
        return name;
    }
}
