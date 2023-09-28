package com.emsapi.unit.domains;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;

import com.emsapi.domains.EmployeeDomain;
import com.emsapi.domains.util.InvalidEmployeeDomainException;

public class EmployeeDomainTests {
    @Test
    public void retornaEmployeeDomain() throws Exception {
        EmployeeDomain employeeDomain = EmployeeDomain.validate(
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com"
        );

        assertThat(employeeDomain.getFirstName()).isEqualTo("foo");
        assertThat(employeeDomain.getLastName()).isEqualTo("bar");
        assertThat(employeeDomain.getAge()).isEqualTo("21");
        assertThat(employeeDomain.getAddress()).isEqualTo("3828 Piermont Dr, Albuquerque, NM");
        assertThat(employeeDomain.getEmail()).isEqualTo("foobar@gmail.com");
    }

    @Test
    public void retornaException_FirstNameInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidEmployeeDomainException.class)
        .isThrownBy(() -> EmployeeDomain.validate(
            "",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com"
        ))
        .withMessage("firstName: must not be blank");
    }

    @Test
    public void retornaException_LastNameInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidEmployeeDomainException.class)
        .isThrownBy(() -> EmployeeDomain.validate(
            "foo",
            "",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com"
        ))
        .withMessage("lastName: must not be blank");
    }

    @Test
    public void retornaException_AgeInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidEmployeeDomainException.class)
        .isThrownBy(() -> EmployeeDomain.validate(
            "foo",
            "bar",
            "",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@gmail.com"
        ))
        .withMessage("age: must not be blank");
    }

    @Test
    public void retornaException_AddressInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidEmployeeDomainException.class)
        .isThrownBy(() -> EmployeeDomain.validate(
            "foo",
            "bar",
            "21",
            "",
            "foobar@gmail.com"
        ))
        .withMessage("address: must not be blank");
    }

    @Test
    public void retornaException_EmailInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidEmployeeDomainException.class)
        .isThrownBy(() -> EmployeeDomain.validate(
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            ""
        ))
        .withMessage("email: must not be blank");
    }

    @Test
    public void retornaException_EmailInvalido_ValorComFormatoInvalido() throws Exception {
        assertThatExceptionOfType(InvalidEmployeeDomainException.class)
        .isThrownBy(() -> EmployeeDomain.validate(
            "foo",
            "bar",
            "21",
            "3828 Piermont Dr, Albuquerque, NM",
            "foobar@"
        ))
        .withMessage("email: must be a well-formed email address");
    }
}
