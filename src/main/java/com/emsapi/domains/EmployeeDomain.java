package com.emsapi.domains;

import java.util.Set;

import com.emsapi.domains.util.InvalidEmployeeDomainException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmployeeDomain {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    
    @NotBlank
    private String age;

    @NotBlank
    private String address;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String roleId;

    private EmployeeDomain(@NotBlank String firstName, @NotBlank String lastName, @NotBlank String age,
            @NotBlank String address, @NotBlank @Email String email, @NotBlank String roleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
        this.email = email;
        this.roleId = roleId;
    }

    public static EmployeeDomain validate(String firstName, String lastName, String age, String address, String email, String roleId) throws InvalidEmployeeDomainException {
        try {
            EmployeeDomain employeeDomain = new EmployeeDomain(firstName, lastName, age, address, email, roleId);

            validation(employeeDomain);

            return employeeDomain;
        } catch (ConstraintViolationException exception) {
            String message = exception.getMessage();

            throw new InvalidEmployeeDomainException(message);
        }
    }

    private static void validation(EmployeeDomain employeeDomain) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<EmployeeDomain>> constraintViolations = validator.validate(employeeDomain);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getRoleId() {
        return roleId;
    }
}
