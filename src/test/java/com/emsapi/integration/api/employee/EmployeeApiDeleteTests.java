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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeApiDeleteTests {
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
        // Enviroment data
        UserModel saveUserModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(saveUserModel.getUserId().toString());

        CompanyModel saveCompanyModel = this.companyRepository.save(CompanyModelBuilder.createWithEmptyCompanyId(saveUserModel));

        RoleModel saveRoleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        EmployeeModel saveEmployeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(saveRoleModel, saveCompanyModel));

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            delete("/api/employee/" + saveEmployeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

		String employeeId = new JSONObject(response.getContentAsString()).getString("employeeId");

        assertThat(response.getStatus()).isEqualTo(200); 
		assertThat(employeeId).isEqualTo(saveEmployeeModel.getEmployeeId().toString());

		Optional<EmployeeModel> findEmployeeModel = this.employeeRepository.findById(UUID.fromString(employeeId));

		assertThat(findEmployeeModel.isEmpty()).isEqualTo(true);
	}

	@Test
	public void retorna404EMesagemDeErro_EmployeeNaoEncontrado_EmployeeNaoCadastrado() throws Exception {
        // Enviroment data
        UserModel saveUserModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(saveUserModel.getUserId().toString());

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            delete("/api/employee/" + UUID.randomUUID().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404); 
		assertThat(response.getContentAsString()).isEqualTo("Employee not found");
	}

	@Test
	public void retorna404EMesagemDeErro_EmployeeNaoEncontrado_UserDaCompanyDiferenteDoInformado() throws Exception {
        // Enviroment data
        UserModel saveUserModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(saveUserModel.getUserId().toString());

		UserModel userModelForCompany = UserModelBuilder.createWithEmptyUserId();
		userModelForCompany.setEmail("barfoo@gmail.com");

		UserModel saveUserModelForCompany = this.userRepository.save(userModelForCompany);

        CompanyModel saveCompanyModel = this.companyRepository.save(CompanyModelBuilder.createWithEmptyCompanyId(saveUserModelForCompany));

        RoleModel saveRoleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        EmployeeModel saveEmployeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(saveRoleModel, saveCompanyModel));

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            delete("/api/employee/" + saveEmployeeModel.getEmployeeId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404); 
		assertThat(response.getContentAsString()).isEqualTo("Employee not found");

		Optional<EmployeeModel> findEmployeeModel = this.employeeRepository.findById(saveEmployeeModel.getEmployeeId());

		assertThat(findEmployeeModel.isEmpty()).isEqualTo(false);
	}
}
