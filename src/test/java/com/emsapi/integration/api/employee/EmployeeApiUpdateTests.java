package com.emsapi.integration.api.employee;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;

import com.emsapi.dtos.employee.UpdateEmployeeDTORequest;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.EmployeeModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UpdateEmployeeDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeApiUpdateTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public String asJsonString(final Object obj) {
        try {
          return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }

    @BeforeEach
    @AfterAll
    public void deleteAll() {
        this.employeeRepository.deleteAll();
        this.roleRepository.deleteAll();
        this.companyRepository.deleteAll();
        this.userRepository.deleteAll();
    }

	@Test
	public void retorna200EEmployeeId() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

		String employeeId = new JSONObject(response.getContentAsString()).getString("employeeId");

		Optional<EmployeeModel> findEmployeeModel = this.employeeRepository.findById(UUID.fromString(employeeId));

		assertThat(findEmployeeModel.get().getFirstName()).isEqualTo(updateEmployeeDTORequest.getFirstName());
		assertThat(findEmployeeModel.get().getLastName()).isEqualTo(updateEmployeeDTORequest.getLastName());
		assertThat(findEmployeeModel.get().getEmail()).isEqualTo(updateEmployeeDTORequest.getEmail());
		assertThat(findEmployeeModel.get().getAge()).isEqualTo(updateEmployeeDTORequest.getAge());
		assertThat(findEmployeeModel.get().getAddress()).isEqualTo(updateEmployeeDTORequest.getAddress());
		assertThat(findEmployeeModel.get().getRoleModel().getRoleId().toString()).isEqualTo(updateEmployeeDTORequest.getRoleId());
		assertThat(findEmployeeModel.get().getCompanyModel().getCompanyId().toString()).isEqualTo(updateEmployeeDTORequest.getCompanyId());
	}
	
	@Test
	public void retorna404EException_EmployeeNaoEncontrado_EmployeeNaoCadastrado() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + UUID.randomUUID().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
		assertThat(response.getContentAsString()).isEqualTo("Employee not found");
	}

	@Test
	public void retorna404EException_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(
			roleModel,
			companyModel	
		));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
			roleModel.getRoleId().toString(),
			UUID.randomUUID().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
 		assertThat(response.getContentAsString()).isEqualTo("Company not found");
	}

	@Test
	public void retorna404EException_CompanyNaoEncontrada_UserDiferenteDoInformado() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));


		UserModel userModelForCompany = UserModelBuilder.createWithEmptyUserId();
		userModelForCompany.setEmail("barfoo@gmail.com");

		UserModel saveUserModelForCompany = this.userRepository.save(userModelForCompany);
		CompanyModel newCompanyModel = this.companyRepository.save(CompanyModelBuilder.createWithEmptyCompanyId(saveUserModelForCompany));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
			roleModel.getRoleId().toString(),
			newCompanyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
		assertThat(response.getContentAsString()).isEqualTo("Company not found");
	}

	@Test
	public void retorna404EException_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithValidData(
			UUID.randomUUID().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
		assertThat(response.getContentAsString()).isEqualTo("Role not found");
	}

	@Test
	public void retorna400EException_FirstNameInvalido_ValorVazio() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithEmptyFirstName(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentAsString()).isEqualTo("firstName: must not be blank");
	}

	@Test
	public void retorna400EException_LastNameInvalido_ValorVazio() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithEmptyLastName(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentAsString()).isEqualTo("lastName: must not be blank");
	}
	
	@Test
	public void retorna400EException_AgeInvalido_ValorVazio() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithEmptyAge(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentAsString()).isEqualTo("age: must not be blank");
	}

	@Test
	public void retorna400EException_AddressInvalido_ValorVazio() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithEmptyAddress(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentAsString()).isEqualTo("address: must not be blank");
	}

	@Test
	public void retorna400EException_EmailInvalido_ValorVazio() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithEmptyEmail(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentAsString()).isEqualTo("email: must not be blank");
	}
	
	@Test
	public void retorna400EException_EmailInvalido_FormatoInvalido() throws Exception {
		// Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

		EmployeeModel employeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(roleModel, companyModel));

        // Test
       	UpdateEmployeeDTORequest updateEmployeeDTORequest = UpdateEmployeeDTORequestBuilder.createWithInvalidEmailFormat(
			roleModel.getRoleId().toString(),
			companyModel.getCompanyId().toString()
		);

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/employee/" + employeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
		assertThat(response.getContentAsString()).isEqualTo("email: must be a well-formed email address");
	}
}


















