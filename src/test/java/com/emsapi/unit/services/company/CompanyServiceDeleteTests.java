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
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.CompanyService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.util.CompanyModelBuilder;

@ExtendWith(SpringExtension.class)
public class CompanyServiceDeleteTests {
    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void retornaCompanyId() throws Exception {
        // Mocks
        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId());
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);
 
        // Test
        DeleteCompanyDTOResponse deleteCompanyDTOResponse = this.companyService.delete(
            companyModelOptionalMock.get().getCompanyId().toString()
        );

        assertThat(deleteCompanyDTOResponse.getCompanyId()).isEqualTo(companyModelOptionalMock.get().getCompanyId().toString());
    }
}
