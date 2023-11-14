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

import com.emsapi.dtos.employee.DeleteEmployeeDTOResponse;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.services.EmployeeService;
import com.emsapi.services.util.EmployeeNotFoundException;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.EmployeeModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceDeleteTests {
	@InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

	@Test
	public void retornaEmployeeId() throws Exception {
		// Mocks
		UserModel userModelMock = UserModelBuilder.createWithUserId();
		CompanyModel companyModelMock = CompanyModelBuilder.createWithCompanyId(userModelMock);
		RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
		Optional<EmployeeModel> employeeModelMock = Optional.of(EmployeeModelBuilder.createWithEmployeeId(roleModelMock, companyModelMock));
		BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(employeeModelMock);

		// Test
		DeleteEmployeeDTOResponse deleteEmployeeDTOResponse = this.employeeService.delete(
			employeeModelMock.get().getEmployeeId().toString(),
			userModelMock.getUserId().toString()
		);

		assertThat(deleteEmployeeDTOResponse.getEmployeeId()).isEqualTo(employeeModelMock.get().getEmployeeId().toString());
	}

	@Test
	public void retornaException_EmployeeNaoEncontrado_EmployeeNaoCadastrado() throws Exception {
		// Mocks
		BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

		// Test
		assertThatExceptionOfType(EmployeeNotFoundException.class)
		.isThrownBy(() -> this.employeeService.delete(UUID.randomUUID().toString(), UUID.randomUUID().toString()))
		.withMessage("Employee not found");
	}
	
	@Test
	public void retornaException_EmployeeNaoEncontrado_UserDaCompanyDiferenteDoInformado() throws Exception {
		// Mocks
		UserModel userModelMock = UserModelBuilder.createWithUserId();
		CompanyModel companyModelMock = CompanyModelBuilder.createWithCompanyId(userModelMock);
		RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
		Optional<EmployeeModel> employeeModelMock = Optional.of(EmployeeModelBuilder.createWithEmployeeId(roleModelMock, companyModelMock));
		BDDMockito.when(this.employeeRepository.findById(ArgumentMatchers.any())).thenReturn(employeeModelMock);

		// Test
		assertThatExceptionOfType(EmployeeNotFoundException.class)
		.isThrownBy(() -> this.employeeService.delete(employeeModelMock.get().getEmployeeId().toString(), UUID.randomUUID().toString()))
		.withMessage("Employee not found");
	}
}
