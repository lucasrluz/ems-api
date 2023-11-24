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

import com.emsapi.dtos.company.UpdateCompanyDTORequest;
import com.emsapi.dtos.company.UpdateCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceUpdateTests {
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

        CompanyModel companyModel = CompanyModelBuilder.createWithEmptyCompanyId(userModeOptionalMock.get());
        companyModel.setCompanyId(companyModelOptionalMock.get().getCompanyId());
        companyModel.setName("bar");
        companyModel.setDescription("foo");
        BDDMockito.when(this.companyRepository.save(ArgumentMatchers.any())).thenReturn(companyModel);
        
        // Test
        UpdateCompanyDTORequest updateCompanyDTORequest = new UpdateCompanyDTORequest("bar", "foo", "foo@gmail.com", "123");

        UpdateCompanyDTOResponse updateCompanyDTOResponse = this.companyService.update(
            updateCompanyDTORequest,
            companyModelOptionalMock.get().getCompanyId().toString(),
            userModeOptionalMock.get().getUserId().toString()
        );

        assertThat(updateCompanyDTOResponse.getCompanyId()).isEqualTo(companyModelOptionalMock.get().getCompanyId().toString());
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
        // Mocks
        Optional<CompanyModel> companyModelOptionalMock = Optional.empty();
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);
        
        // Test
        UpdateCompanyDTORequest updateCompanyDTORequest = new UpdateCompanyDTORequest("bar", "foo","foo@gmail.com", "123");

        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.companyService.update(updateCompanyDTORequest, UUID.randomUUID().toString(), UUID.randomUUID().toString()))
        .withMessage("Company not found");
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_UsuarioDaCompanyDiferenteDoInformado() throws Exception {
        // Mocks
        UserModel userModelForCompanyModel = UserModelBuilder.createWithUserId();
        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelForCompanyModel));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);
        
        Optional<UserModel> userModeOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModeOptionalMock);

        CompanyModel companyModel = CompanyModelBuilder.createWithEmptyCompanyId(userModelForCompanyModel);
        companyModel.setCompanyId(companyModelOptionalMock.get().getCompanyId());
        companyModel.setName("bar");
        companyModel.setDescription("foo");
        BDDMockito.when(this.companyRepository.save(ArgumentMatchers.any())).thenReturn(companyModel);
        
        // Test
        UpdateCompanyDTORequest updateCompanyDTORequest = new UpdateCompanyDTORequest("bar", "foo", "foo@gmail.com", "123");

        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.companyService.update(updateCompanyDTORequest, companyModelOptionalMock.get().getCompanyId().toString(), userModeOptionalMock.get().getUserId().toString()))
        .withMessage("Company not found");
    }
}
