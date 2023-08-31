package com.emsapi.unit.domains;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;

import com.emsapi.domains.UserDomain;
import com.emsapi.domains.util.InvalidUserDomainException;

public class UserDomainTests {
    @Test
    public void retornaUserDomain() throws Exception {
        UserDomain userDomain = UserDomain.validate("foo", "bar", "foobar@gmail.com", "123");

        assertThat(userDomain.getFirstName()).isEqualTo("foo");
        assertThat(userDomain.getLastName()).isEqualTo("bar");
        assertThat(userDomain.getEmail()).isEqualTo("foobar@gmail.com");
        assertThat(userDomain.getPassword()).isEqualTo("123");
    }

    @Test
    public void retornaException_FirstNameInvalido_ValorVazio() {
        assertThatExceptionOfType(InvalidUserDomainException.class)
        .isThrownBy(() -> UserDomain.validate("", "bar", "foobar@gmail.com", "123"))
        .withMessage("firstName: must not be blank");
    }

    @Test
    public void retornaException_LastNameInvalido_ValorVazio() {
        assertThatExceptionOfType(InvalidUserDomainException.class)
        .isThrownBy(() -> UserDomain.validate("foo", "", "foobar@gmail.com", "123"))
        .withMessage("lastName: must not be blank");
    }

    @Test
    public void retornaException_EmailInvalido_ValorVazio() {
        assertThatExceptionOfType(InvalidUserDomainException.class)
        .isThrownBy(() -> UserDomain.validate("foo", "bar", "", "123"))
        .withMessage("email: must not be blank");
    }

    @Test
    public void retornaException_EmailInvalido_ValorComFormatoInvalido() {
        assertThatExceptionOfType(InvalidUserDomainException.class)
        .isThrownBy(() -> UserDomain.validate("foo", "bar", "foobar@", "123"))
        .withMessage("email: must be a well-formed email address");
    }

    @Test
    public void retornaException_PasswordInvalida_ValorVazio() {
        assertThatExceptionOfType(InvalidUserDomainException.class)
        .isThrownBy(() -> UserDomain.validate("foo", "bar", "foobar@gmail.com", ""))
        .withMessage("password: must not be blank");
    }
}
