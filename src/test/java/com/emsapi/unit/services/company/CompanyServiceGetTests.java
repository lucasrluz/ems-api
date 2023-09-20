package com.emsapi.unit.services.company;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.company.GetCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceGetTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaCompany() throws Exception {
        // Mocks
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelOptionalMock.get()));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        // Test
        GetCompanyDTOResponse getCompanyDTOResponse = this.companyService.get(
            companyModelOptionalMock.get().getCompanyId().toString(),
            userModelOptionalMock.get().getUserId().toString()
        );

        assertThat(getCompanyDTOResponse.getCompanyId()).isEqualTo(companyModelOptionalMock.get().getCompanyId().toString());
        assertThat(getCompanyDTOResponse.getName()).isEqualTo(companyModelOptionalMock.get().getName());
        assertThat(getCompanyDTOResponse.getDescription()).isEqualTo(companyModelOptionalMock.get().getDescription());
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
        // Mocks
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        Optional<CompanyModel> companyModelOptionalMock = Optional.empty();
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.companyService.get(UUID.randomUUID().toString(), userModelOptionalMock.get().getUserId().toString()))
        .withMessage("Company not found");
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_UserIdDiferenteDoInformado() throws Exception {
        // Mocks
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId(UserModelBuilder.createWithUserId()));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.companyService.get(UUID.randomUUID().toString(), userModelOptionalMock.get().getUserId().toString()))
        .withMessage("Company not found");
    }
}
