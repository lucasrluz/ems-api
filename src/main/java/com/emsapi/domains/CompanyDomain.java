package com.emsapi.domains;

import java.util.Set;

import com.emsapi.domains.util.InvalidCompanyDomainException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CompanyDomain {
    @NotBlank
    private String name;
    
    @NotBlank
    private String description;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

    private CompanyDomain(@NotBlank String name, @NotBlank String description, @NotBlank @Email String email, @NotBlank String password) {
        this.name = name;
        this.description = description;
		this.email = email;
		this.password = password;
    }

    public static CompanyDomain validate(String name, String description, String email, String password) throws InvalidCompanyDomainException {
        try {
            CompanyDomain companyDomain = new CompanyDomain(name, description, email, password);

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

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
