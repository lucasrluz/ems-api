package com.emsapi.unit.domains;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;

import com.emsapi.domains.RoleDomain;
import com.emsapi.domains.util.InvalidRoleDomainException;

public class RoleDomainTests {
    @Test
    public void retornaRoleDomain() throws Exception {
        RoleDomain roleDomain = RoleDomain.validate("foo");

        assertThat(roleDomain.getName()).isEqualTo("foo");
    }

    @Test
    public void retornaException_NameInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidRoleDomainException.class)
        .isThrownBy(() -> RoleDomain.validate(""))
        .withMessage("name: must not be blank");
    }
}
