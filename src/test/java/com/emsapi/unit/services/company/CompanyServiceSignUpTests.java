package com.emsapi.unit.services.company;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.authentication.SignUpDTORequest;
import com.emsapi.dtos.authentication.SignUpDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.SignUpDTORequestBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceSignUpTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void retornaCompanyId() throws Exception {
        // Mocks
        CompanyModel companyModel = CompanyModelBuilder.createWithCompanyId();
        BDDMockito.when(this.companyRepository.save(ArgumentMatchers.any())).thenReturn(companyModel);

        // Tests
		SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();

        SignUpDTOResponse signUpDTOResponse = this.companyService.signUp(
        	signUpDTORequest 
        );

        assertThat(signUpDTOResponse.getCompanyId()).isEqualTo(companyModel.getCompanyId().toString());
    }
}
