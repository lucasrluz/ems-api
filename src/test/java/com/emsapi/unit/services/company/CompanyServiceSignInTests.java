package com.emsapi.unit.services.company;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.authentication.SignInDTORequest;
import com.emsapi.dtos.authentication.SignInDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.services.JwtService;
import com.emsapi.services.util.EmailOrPasswordInvalidException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.SignInDTORequestBuilder;

import at.favre.lib.crypto.bcrypt.BCrypt;

@ExtendWith(SpringExtension.class)
public class CompanyServiceSignInTests {
    @InjectMocks
    private CompanyService companyService;

	@Mock
	private JwtService jwtService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void retornaJWT() throws Exception {
        // Mocks
        CompanyModel companyModel = CompanyModelBuilder.createWithCompanyId();
		BDDMockito.when(this.companyRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.of(companyModel));

		BDDMockito.when(this.jwtService.generateJwt(ArgumentMatchers.any())).thenReturn("fake-jwt");

        // Tests
		SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

        SignInDTOResponse signInDTOResponse = this.companyService.signIn(
        	signInDTORequest	
        );

		assertThat(signInDTOResponse.getJwt()).isEqualTo("fake-jwt");
    }

    @Test
    public void retornaException_EmailNaoEncontrado_EmailNaoCadastrado() throws Exception {
        // Mocks
		BDDMockito.when(this.companyRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Tests
		SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

		assertThatExceptionOfType(EmailOrPasswordInvalidException.class)
			.isThrownBy(() -> this.companyService.signIn(signInDTORequest))
			.withMessage("Email or password invalid");
    }

	@Test
    public void retornaException_PasswordInvalida() throws Exception {
        // Mocks
        CompanyModel companyModel = CompanyModelBuilder.createWithCompanyId();
		companyModel.setPassword(BCrypt.withDefaults().hashToString(12, "456".toCharArray()));
		BDDMockito.when(this.companyRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.of(companyModel));

        // Tests
		SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

		assertThatExceptionOfType(EmailOrPasswordInvalidException.class)
			.isThrownBy(() -> this.companyService.signIn(signInDTORequest))
			.withMessage("Email or password invalid");

    }
}
