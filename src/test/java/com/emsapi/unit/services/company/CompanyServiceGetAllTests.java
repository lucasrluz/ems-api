package com.emsapi.unit.services.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.company.GetAllCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceGetAllTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaListaDeCompany() {
        // Mocks
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        List<CompanyModel> companyModels = new ArrayList<CompanyModel>();

        companyModels.add(CompanyModelBuilder.createWithCompanyId(userModelOptionalMock.get()));
        companyModels.add(CompanyModelBuilder.createWithCompanyId(userModelOptionalMock.get()));
        companyModels.add(CompanyModelBuilder.createWithCompanyId(userModelOptionalMock.get()));

        BDDMockito.when(this.companyRepository.findByUserModel(ArgumentMatchers.any())).thenReturn(companyModels);

        // Test
        List<GetAllCompanyDTOResponse> getAllCompanyDTOResponses = this.companyService.getAll(
            userModelOptionalMock.get().getUserId().toString()
        );

        assertThat(getAllCompanyDTOResponses.get(0).getCompanyId()).isEqualTo(companyModels.get(0).getCompanyId().toString());
        assertThat(getAllCompanyDTOResponses.get(0).getName()).isEqualTo(companyModels.get(0).getName());
        assertThat(getAllCompanyDTOResponses.get(0).getDescription()).isEqualTo(companyModels.get(0).getDescription());
        
        assertThat(getAllCompanyDTOResponses.get(1).getCompanyId()).isEqualTo(companyModels.get(1).getCompanyId().toString());
        assertThat(getAllCompanyDTOResponses.get(1).getName()).isEqualTo(companyModels.get(1).getName());
        assertThat(getAllCompanyDTOResponses.get(1).getDescription()).isEqualTo(companyModels.get(1).getDescription());

        assertThat(getAllCompanyDTOResponses.get(2).getCompanyId()).isEqualTo(companyModels.get(2).getCompanyId().toString());
        assertThat(getAllCompanyDTOResponses.get(2).getName()).isEqualTo(companyModels.get(2).getName());
        assertThat(getAllCompanyDTOResponses.get(2).getDescription()).isEqualTo(companyModels.get(2).getDescription());
    }

    @Test
    public void retornaListaVazia() {
        // Mocks
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        List<CompanyModel> companyModels = new ArrayList<CompanyModel>();
        BDDMockito.when(this.companyRepository.findByUserModel(ArgumentMatchers.any())).thenReturn(companyModels);

        // Test
        List<GetAllCompanyDTOResponse> getAllCompanyDTOResponses = this.companyService.getAll(
            userModelOptionalMock.get().getUserId().toString()
        );

        assertThat(getAllCompanyDTOResponses.isEmpty()).isEqualTo(true); 
    }
}
