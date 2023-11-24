package com.emsapi.unit.domains;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;

import com.emsapi.domains.CompanyDomain;
import com.emsapi.domains.util.InvalidCompanyDomainException;

public class CompanyDomainTests {
    @Test
    public void retornaCompanyDomain() throws Exception {
        CompanyDomain companyDomain = CompanyDomain.validate("foo", "foo bar", "foo@gmail.com", "123");

        assertThat(companyDomain.getName()).isEqualTo("foo");
        assertThat(companyDomain.getDescription()).isEqualTo("foo bar");
    }

    @Test
    public void retornaException_NameInvalido_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidCompanyDomainException.class)
        .isThrownBy(() -> CompanyDomain.validate("", "foo bar", "foo@gmail.com", "123"))
        .withMessage("name: must not be blank");
    }

    @Test
    public void retornaException_DescriptionInvalida_ValorVazio() throws Exception {
        assertThatExceptionOfType(InvalidCompanyDomainException.class)
        .isThrownBy(() -> CompanyDomain.validate("foo", "", "foo@gmail.com", "123"))
        .withMessage("description: must not be blank");
    }

	@Test
	public void retornaException_EmailComFormatoInvalido() throws Exception {
		assertThatExceptionOfType(InvalidCompanyDomainException.class)
        .isThrownBy(() -> CompanyDomain.validate("foo", "foo bar", "@gmail.com", "123"))
        .withMessage("email: must be a well-formed email address");
	}

	@Test
	public void retornaException_EmailComValorVazio() throws Exception {
		assertThatExceptionOfType(InvalidCompanyDomainException.class)
        .isThrownBy(() -> CompanyDomain.validate("foo", "foo bar", "", "123"))
        .withMessage("email: must not be blank");
	}

	@Test
	public void retornaException_PasswordComValorVazio() throws Exception {
		assertThatExceptionOfType(InvalidCompanyDomainException.class)
        .isThrownBy(() -> CompanyDomain.validate("foo", "foo bar", "foo@gmail.com", ""))
        .withMessage("password: must not be blank");
	}
}
