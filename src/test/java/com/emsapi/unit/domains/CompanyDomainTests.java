package com.emsapi.unit.domains;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;

import com.emsapi.domains.CompanyDomain;
import com.emsapi.domains.util.InvalidCompanyDomainException;

public class CompanyDomainTests {
    @Test
    public void retornaCompanyDomain() throws Exception {
        CompanyDomain companyDomain = CompanyDomain.validate("foo", "foo bar");

        assertThat(companyDomain.getName()).isEqualTo("foo");
        assertThat(companyDomain.getDescription()).isEqualTo("foo bar");
    }

    @Test
    public void retornaException_NameInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidCompanyDomainException.class)
        .isThrownBy(() -> CompanyDomain.validate("", "foo bar"))
        .withMessage("name: must not be blank");
    }

    @Test
    public void retornaException_DescriptionInvalida_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidCompanyDomainException.class)
        .isThrownBy(() -> CompanyDomain.validate("foo", ""))
        .withMessage("description: must not be blank");
    }
}
