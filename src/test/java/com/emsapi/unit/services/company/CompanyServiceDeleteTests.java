package com.emsapi.unit.services.company;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import com.emsapi.dtos.company.DeleteCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceDeleteTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaCompanyId() throws Exception {
        // Mocks
        Optional<UserModel> userModeOptionalMock = Optional.of(UserModelBuilder.createWithUserId());

        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModeOptionalMock.get()));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModeOptionalMock);
        
        // Test
        DeleteCompanyDTOResponse deleteCompanyDTOResponse = this.companyService.delete(
            companyModelOptionalMock.get().getCompanyId().toString(),
            userModeOptionalMock.get().getUserId().toString()
        );

        assertThat(deleteCompanyDTOResponse.getCompanyId()).isEqualTo(companyModelOptionalMock.get().getCompanyId().toString());
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
        // Mocks
        Optional<CompanyModel> companyModelOptionalMock = Optional.empty();
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);
        
        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.companyService.delete(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
        .withMessage("Company not found");
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_UsuarioDiferenteDoInformado() throws Exception {
        // Mocks
        UserModel userModeOptionalForCompanyModel = UserModelBuilder.createWithUserId();
        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModeOptionalForCompanyModel));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        Optional<UserModel> userModeOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModeOptionalMock);
        
        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.companyService.delete(userModeOptionalMock.get().getUserId().toString(), companyModelOptionalMock.get().getCompanyId().toString()))
        .withMessage("Company not found");
    }
}
