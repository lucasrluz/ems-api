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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import com.emsapi.dtos.employee.SaveEmployeeDTORequest;
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
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.SaveEmployeeDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeApiSaveTests {
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
    public void retorna201EEmployeeId() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        // Test
        SaveEmployeeDTORequest saveEmployeeDTORequest = SaveEmployeeDTORequestBuilder.createWithValidData(
            companyModel.getCompanyId().toString(),
            roleModel.getRoleId().toString()
        );

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/employee")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        String employeeId = new JSONObject(response.getContentAsString()).getString("employeeId");
        Optional<EmployeeModel> findEmployeeById = this.employeeRepository.findById(UUID.fromString(employeeId));

        assertThat(findEmployeeById.get().getFirstName()).isEqualTo(saveEmployeeDTORequest.getFirstName());
        assertThat(findEmployeeById.get().getLastName()).isEqualTo(saveEmployeeDTORequest.getLastName());
        assertThat(findEmployeeById.get().getAge()).isEqualTo(saveEmployeeDTORequest.getAge());
        assertThat(findEmployeeById.get().getAddress()).isEqualTo(saveEmployeeDTORequest.getAddress());
        assertThat(findEmployeeById.get().getEmail()).isEqualTo(saveEmployeeDTORequest.getEmail());
    }

    @Test
    public void retorna404EMesagemDeErro_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        // Test
        SaveEmployeeDTORequest saveEmployeeDTORequest = SaveEmployeeDTORequestBuilder.createWithValidData(
            companyModel.getCompanyId().toString(),
            UUID.randomUUID().toString()
        );

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/employee")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Role not found");
    }

    @Test
    public void retorna404EMensagemDeErro_CompanyNaoEcontrada_CompanyNaoCadastrada() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        // Test
        SaveEmployeeDTORequest saveEmployeeDTORequest = SaveEmployeeDTORequestBuilder.createWithValidData(
            UUID.randomUUID().toString(),
            roleModel.getRoleId().toString()
        );

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/employee")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveEmployeeDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Company not found");
    }
}
