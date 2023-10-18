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

import com.emsapi.dtos.employee.UpdateEmployeeDTORequest;
import com.emsapi.dtos.employee.UpdateEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.EmployeeService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.EmployeeNotFoundException;
import com.emsapi.services.util.RoleNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.EmployeeModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UpdateEmployeeDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceUpdateTests {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void retornaEmployeeId() throws Exception {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithUserId();
        RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
        CompanyModel companyModelMock = CompanyModelBuilder.createWithCompanyId(userModelMock);
        Optional<EmployeeModel> employeeModelMock = Optional.of(EmployeeModelBuilder.createWithEmployeeId(roleModelMock, companyModelMock));
        BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(employeeModelMock);

        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(companyModelMock));
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(roleModelMock));

        RoleModel newRoleModelMock = RoleModelBuilder.createWithRoleId();
        CompanyModel newCompanyModelMock = CompanyModelBuilder.createWithCompanyId(userModelMock);

        employeeModelMock.get().setFirstName("bar");
        employeeModelMock.get().setLastName("foo");
        employeeModelMock.get().setAge("22");
        employeeModelMock.get().setAddress("9809 Margo Street");
        employeeModelMock.get().setEmail("barfoo@gmail.com");
        employeeModelMock.get().setRoleModel(newRoleModelMock);
        employeeModelMock.get().setCompanyModel(newCompanyModelMock);

        BDDMockito.when(this.employeeRepository.save(ArgumentMatchers.any())).thenReturn(employeeModelMock.get());
        
        // Test
        UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
            newRoleModelMock.getRoleId().toString(),
            newCompanyModelMock.getCompanyId().toString()
        );

        UpdateEmployeeDTOResponse updateEmployeeDTOResponse = this.employeeService.update(
            updateEmployeeDTORequest,
            employeeModelMock.get().getEmployeeId().toString(),
            userModelMock.getUserId().toString()
        );

        assertThat(updateEmployeeDTOResponse.getEmployeeId()).isEqualTo(employeeModelMock.get().getEmployeeId().toString());
    }

    @Test
    public void retornaException_EmployeeNaoEncontrado_EmployeeNaoCadastrado() throws Exception {
        // Mocks
        BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Test
        UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );

        assertThatExceptionOfType(EmployeeNotFoundException.class)
        .isThrownBy(() -> this.employeeService.update(
            updateEmployeeDTORequest,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        ))
        .withMessage("Employee not found");
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithUserId();
        RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
        CompanyModel companyModelMock = CompanyModelBuilder.createWithCompanyId(userModelMock);
        Optional<EmployeeModel> employeeModelMock = Optional.of(EmployeeModelBuilder.createWithEmployeeId(roleModelMock, companyModelMock));
        BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(employeeModelMock);

        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        
        // Test
        UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );

        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.employeeService.update(
            updateEmployeeDTORequest,
            employeeModelMock.get().getEmployeeId().toString(),
            userModelMock.getUserId().toString()
        ))
        .withMessage("Company not found");
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_UserDiferenteDoInformado() throws Exception {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithUserId();
        RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
        CompanyModel companyModelMock = CompanyModelBuilder.createWithCompanyId(userModelMock);
        Optional<EmployeeModel> employeeModelMock = Optional.of(EmployeeModelBuilder.createWithEmployeeId(roleModelMock, companyModelMock));
        BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(employeeModelMock);

        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(companyModelMock));
       
        // Test
        UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        ); 

        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.employeeService.update(
            updateEmployeeDTORequest,
            employeeModelMock.get().getEmployeeId().toString(),
            UUID.randomUUID().toString()
        ))
        .withMessage("Company not found");
    }

    @Test
    public void retornaException_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Mocks
        UserModel userModelMock = UserModelBuilder.createWithUserId();
        RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
        CompanyModel companyModelMock = CompanyModelBuilder.createWithCompanyId(userModelMock);
        Optional<EmployeeModel> employeeModelMock = Optional.of(EmployeeModelBuilder.createWithEmployeeId(roleModelMock, companyModelMock));
        BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(employeeModelMock);

        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(companyModelMock));
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        
        // Test
        UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        );

        assertThatExceptionOfType(RoleNotFoundException.class)
        .isThrownBy(() -> this.employeeService.update(
            updateEmployeeDTORequest,
            employeeModelMock.get().getEmployeeId().toString(),
            userModelMock.getUserId().toString()
        ))
        .withMessage("Role not found");
    }
}







