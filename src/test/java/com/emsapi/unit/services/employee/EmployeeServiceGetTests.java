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

import com.emsapi.dtos.employee.GetEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.services.EmployeeService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.EmployeeNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.EmployeeModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceGetTests {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void retornaEmployee() throws Exception {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithUserId();
        Optional<CompanyModel> companyModelMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelMock));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelMock);

        RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
        Optional<EmployeeModel> employeeModelMock = Optional.of(EmployeeModelBuilder.createWithEmployeeId(roleModelMock, companyModelMock.get()));
        BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(employeeModelMock);
        
        // Test
        GetEmployeeDTOResponse getEmployeeDTOResponse = this.employeeService.get(
            employeeModelMock.get().getEmployeeId().toString(),
            companyModelMock.get().getCompanyId().toString(),
            userModelMock.getUserId().toString() 
        );

        assertThat(getEmployeeDTOResponse.getEmployeeId()).isEqualTo(employeeModelMock.get().getEmployeeId().toString());
        assertThat(getEmployeeDTOResponse.getFirstName()).isEqualTo(employeeModelMock.get().getFirstName());
        assertThat(getEmployeeDTOResponse.getLastName()).isEqualTo(employeeModelMock.get().getLastName());
        assertThat(getEmployeeDTOResponse.getAge()).isEqualTo(employeeModelMock.get().getAge());
        assertThat(getEmployeeDTOResponse.getAddress()).isEqualTo(employeeModelMock.get().getAddress());
        assertThat(getEmployeeDTOResponse.getCompanyId()).isEqualTo(employeeModelMock.get().getCompanyModel().getCompanyId().toString());
        assertThat(getEmployeeDTOResponse.getEmail()).isEqualTo(employeeModelMock.get().getEmail());
        assertThat(getEmployeeDTOResponse.getRoleId()).isEqualTo(employeeModelMock.get().getRoleModel().getRoleId().toString());
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
        // Mocks
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
 
        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.employeeService.get(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        ))
        .withMessage("Company not found");

    }

    @Test
    public void retornaException_CompanyNaoEncontrada_UserDaCompanyDiferenteDoInformado() throws Exception {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithUserId();
        Optional<CompanyModel> companyModelMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelMock));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelMock);
 
        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.employeeService.get(
            UUID.randomUUID().toString(),
            companyModelMock.get().getCompanyId().toString(),
            UUID.randomUUID().toString()
        ))
        .withMessage("Company not found");
    }

    @Test
    public void retornaException_EmployeeNaoEncontrado_EmployeeNaoCadastrado() throws Exception {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithUserId();
        Optional<CompanyModel> companyModelMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelMock));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelMock);

        BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        
        // Test
        assertThatExceptionOfType(EmployeeNotFoundException.class)
        .isThrownBy(() -> this.employeeService.get(
            UUID.randomUUID().toString(),
            companyModelMock.get().getCompanyId().toString(),
            userModelMock.getUserId().toString()
        ))
        .withMessage("Employee not found");
    }
}

