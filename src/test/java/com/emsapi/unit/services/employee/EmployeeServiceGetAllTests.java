package com.emsapi.unit.services.employee;

import java.util.ArrayList;
import java.util.List;
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

import com.emsapi.dtos.employee.GetAllEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.EmployeeService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.EmployeeModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceGetAllTests {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaListaComEmployees() throws CompanyNotFoundException {
        // Mocks
        Optional<UserModel> userModelMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelMock);

        Optional<CompanyModel> companyModelMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelMock.get()));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelMock);

        RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
        List<EmployeeModel> employeeModels = EmployeeModelBuilder.createListWithEmployeeId(roleModelMock, companyModelMock.get());
        BDDMockito.when(this.employeeRepository.findByCompanyModel(ArgumentMatchers.any())).thenReturn(employeeModels);
        
        // Test
        List<GetAllEmployeeDTOResponse> getAllEmployeeDTOResponse = this.employeeService.getAll(
            companyModelMock.get().getCompanyId().toString(),
            userModelMock.get().getUserId().toString()
        );

        assertThat(getAllEmployeeDTOResponse.get(0).getFirstName()).isEqualTo(employeeModels.get(0).getFirstName());
        assertThat(getAllEmployeeDTOResponse.get(0).getLastName()).isEqualTo(employeeModels.get(0).getLastName());
        assertThat(getAllEmployeeDTOResponse.get(0).getAge()).isEqualTo(employeeModels.get(0).getAge());
        assertThat(getAllEmployeeDTOResponse.get(0).getAddress()).isEqualTo(employeeModels.get(0).getAddress());
        assertThat(getAllEmployeeDTOResponse.get(0).getEmail()).isEqualTo(employeeModels.get(0).getEmail());
        assertThat(getAllEmployeeDTOResponse.get(0).getCompanyId()).isEqualTo(employeeModels.get(0).getCompanyModel().getCompanyId().toString());
        assertThat(getAllEmployeeDTOResponse.get(0).getRoleId()).isEqualTo(employeeModels.get(0).getRoleModel().getRoleId().toString());

        assertThat(getAllEmployeeDTOResponse.get(1).getFirstName()).isEqualTo(employeeModels.get(1).getFirstName());
        assertThat(getAllEmployeeDTOResponse.get(1).getLastName()).isEqualTo(employeeModels.get(1).getLastName());
        assertThat(getAllEmployeeDTOResponse.get(1).getAge()).isEqualTo(employeeModels.get(1).getAge());
        assertThat(getAllEmployeeDTOResponse.get(1).getAddress()).isEqualTo(employeeModels.get(1).getAddress());
        assertThat(getAllEmployeeDTOResponse.get(1).getEmail()).isEqualTo(employeeModels.get(1).getEmail());
        assertThat(getAllEmployeeDTOResponse.get(1).getCompanyId()).isEqualTo(employeeModels.get(1).getCompanyModel().getCompanyId().toString());
        assertThat(getAllEmployeeDTOResponse.get(1).getRoleId()).isEqualTo(employeeModels.get(1).getRoleModel().getRoleId().toString());

        assertThat(getAllEmployeeDTOResponse.get(2).getFirstName()).isEqualTo(employeeModels.get(2).getFirstName());
        assertThat(getAllEmployeeDTOResponse.get(2).getLastName()).isEqualTo(employeeModels.get(2).getLastName());
        assertThat(getAllEmployeeDTOResponse.get(2).getAge()).isEqualTo(employeeModels.get(2).getAge());
        assertThat(getAllEmployeeDTOResponse.get(2).getAddress()).isEqualTo(employeeModels.get(2).getAddress());
        assertThat(getAllEmployeeDTOResponse.get(2).getEmail()).isEqualTo(employeeModels.get(2).getEmail());
        assertThat(getAllEmployeeDTOResponse.get(2).getCompanyId()).isEqualTo(employeeModels.get(2).getCompanyModel().getCompanyId().toString());
        assertThat(getAllEmployeeDTOResponse.get(2).getRoleId()).isEqualTo(employeeModels.get(2).getRoleModel().getRoleId().toString());
    }

    @Test
    public void retornaListaVazia() throws CompanyNotFoundException {
        // Mocks
        Optional<UserModel> userModelMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelMock);

        Optional<CompanyModel> companyModelMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelMock.get()));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelMock);

        List<EmployeeModel> employeeModels = new ArrayList<EmployeeModel>();
        BDDMockito.when(this.employeeRepository.findByCompanyModel(ArgumentMatchers.any())).thenReturn(employeeModels);
        
        // Test
        List<GetAllEmployeeDTOResponse> getAllEmployeeDTOResponse = this.employeeService.getAll(
            companyModelMock.get().getCompanyId().toString(),
            userModelMock.get().getUserId().toString()
        );

        assertThat(getAllEmployeeDTOResponse.isEmpty()).isEqualTo(true);
    }

    @Test
    public void retornaException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws CompanyNotFoundException {
        // Mocks
        Optional<UserModel> userModelMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelMock);

        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        
        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.employeeService.getAll(
            UUID.randomUUID().toString(),
            userModelMock.get().getUserId().toString()
        ))
        .withMessage("Company not found");
    }

    @Test
    public void retornaException_UserDaCompanyDiferenteDoUserInformado() throws CompanyNotFoundException {
        // Mocks
        Optional<UserModel> userModelMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelMock);

        UserModel userModelForCompanyMock = UserModelBuilder.createWithUserId();
        Optional<CompanyModel> companyModelMock = Optional.of(CompanyModelBuilder.createWithCompanyId(userModelForCompanyMock));
        BDDMockito.when(this.companyRepository.findById(ArgumentMatchers.any())).thenReturn(companyModelMock);
        
        // Test
        assertThatExceptionOfType(CompanyNotFoundException.class)
        .isThrownBy(() -> this.employeeService.getAll(
            UUID.randomUUID().toString(),
            userModelMock.get().getUserId().toString()
        ))
        .withMessage("Company not found");
    }
}
