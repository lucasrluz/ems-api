package com.emsapi.unit.services.company;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.company.SaveCompanyDTORequest;
import com.emsapi.dtos.company.SaveCompanyDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceSaveTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaCompanyId() throws Exception {
        // Mocks
        Optional<UserModel> userModel = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModel);

        CompanyModel companyModel = CompanyModelBuilder.createWithCompanyId(userModel.get());
        BDDMockito.when(this.companyRepository.save(ArgumentMatchers.any())).thenReturn(companyModel);

        // Tests
        SaveCompanyDTORequest saveCompanyDTORequest = new SaveCompanyDTORequest("foo", "bar", "foo@gmail.com", "123");

        SaveCompanyDTOResponse saveCompanyDTOResponse = this.companyService.save(
            saveCompanyDTORequest,
            userModel.get().getUserId().toString()
        );

        assertThat(saveCompanyDTOResponse.getCompanyId()).isEqualTo(companyModel.getCompanyId().toString());
    }
}
