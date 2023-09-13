package com.emsapi.domains;

import java.util.Set;

import com.emsapi.domains.util.InvalidCompanyDomainException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;

public class CompanyDomain {
    @NotBlank
    private String name;
    
    @NotBlank
    private String description;

    private CompanyDomain(@NotBlank String name, @NotBlank String description) {
        this.name = name;
        this.description = description;
    }

    public static CompanyDomain validate(String name, String description) throws InvalidCompanyDomainException {
        try {
            CompanyDomain companyDomain = new CompanyDomain(name, description);

            validation(companyDomain);

            return companyDomain;
        } catch (ConstraintViolationException exception) {
            String message = exception.getMessage();

            throw new InvalidCompanyDomainException(message);
        }
    }

    private static void validation(CompanyDomain companyDomain) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<CompanyDomain>> constraintViolations = validator.validate(companyDomain);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
