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
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.util.CompanyModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceGetTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void retornaCompany() throws Exception {
        // Mocks
        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId());
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        // Test
        GetCompanyDTOResponse getCompanyDTOResponse = this.companyService.get(
            companyModelOptionalMock.get().getCompanyId().toString()
        );

        assertThat(getCompanyDTOResponse.getCompanyId()).isEqualTo(companyModelOptionalMock.get().getCompanyId().toString());
        assertThat(getCompanyDTOResponse.getName()).isEqualTo(companyModelOptionalMock.get().getName());
        assertThat(getCompanyDTOResponse.getDescription()).isEqualTo(companyModelOptionalMock.get().getDescription());
    }
}
