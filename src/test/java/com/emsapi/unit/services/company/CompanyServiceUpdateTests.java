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
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.util.CompanyModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceUpdateTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void retornaCompanyId() throws Exception {
        // Mocks
        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId());
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        CompanyModel companyModel = CompanyModelBuilder.createWithEmptyCompanyId();
        companyModel.setCompanyId(companyModelOptionalMock.get().getCompanyId());
        companyModel.setName("bar");
        companyModel.setDescription("foo");
        BDDMockito.when(this.companyRepository.save(ArgumentMatchers.any())).thenReturn(companyModel);
        
        // Test
        UpdateCompanyDTORequest updateCompanyDTORequest = new UpdateCompanyDTORequest("bar", "foo", "foo@gmail.com", "123");

        UpdateCompanyDTOResponse updateCompanyDTOResponse = this.companyService.update(
            updateCompanyDTORequest,
            companyModelOptionalMock.get().getCompanyId().toString()
        );

        assertThat(updateCompanyDTOResponse.getCompanyId()).isEqualTo(companyModelOptionalMock.get().getCompanyId().toString());
    }
}
