package com.emsapi.unit.services.employee;

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

import com.emsapi.dtos.employee.SaveEmployeeDTORequest;
import com.emsapi.dtos.employee.SaveEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.EmployeeService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.RoleNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.EmployeeModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.SaveEmployeeDTORequestBuilder;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceSaveTests {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void retornaEmployeeId() throws Exception {
        // Mocks
        Optional<RoleModel> roleModelOptionalMock = Optional.of(RoleModelBuilder.createWithRoleId());
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock);

        Optional<CompanyModel> companyModelOptionalMock = Optional.of(CompanyModelBuilder.createWithCompanyId());
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelOptionalMock);

        EmployeeModel employeeModelMock = EmployeeModelBuilder.createWithEmployeeId(
            roleModelOptionalMock.get(),
            companyModelOptionalMock.get()
        );
        BDDMockito.when(this.employeeRepository.save(ArgumentMatchers.any())).thenReturn(employeeModelMock);

        // Test
        SaveEmployeeDTORequest saveEmployeeDTORequest = SaveEmployeeDTORequestBuilder.createWithValidData(
            companyModelOptionalMock.get().getCompanyId().toString(),
            roleModelOptionalMock.get().getRoleId().toString()    
        );

        SaveEmployeeDTOResponse saveEmployeeDTOResponse = this.employeeService.save(saveEmployeeDTORequest);
        
        assertThat(saveEmployeeDTOResponse.getEmployeeId()).isEqualTo(employeeModelMock.getEmployeeId().toString());
    }

    @Test
    public void retornaException_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Mocks
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Test
        SaveEmployeeDTORequest saveEmployeeDTORequest = SaveEmployeeDTORequestBuilder.createWithValidData(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()    
        );

        assertThatExceptionOfType(RoleNotFoundException.class)
        .isThrownBy(() -> this.employeeService.save(saveEmployeeDTORequest))
        .withMessage("Role not found");
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
        // Mocks
        Optional<RoleModel> roleModelOptionalMock = Optional.of(RoleModelBuilder.createWithRoleId());
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock);

        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Test
        SaveEmployeeDTORequest saveEmployeeDTORequest = SaveEmployeeDTORequestBuilder.createWithValidData(
            UUID.randomUUID().toString(),
            roleModelOptionalMock.get().getRoleId().toString()    
        );
        
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.employeeService.save(saveEmployeeDTORequest))
        .withMessage("Company not found");
    }
}
