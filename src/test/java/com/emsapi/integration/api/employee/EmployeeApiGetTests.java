package com.emsapi.integration.api.employee;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

import com.emsapi.models.CompanyModel;
import com.emsapi.models.EmployeeModel;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.EmployeeRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.EmployeeModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeApiGetTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

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
    }

    @Test
    public void retorna200EEmployee() throws Exception {
        // Enviroment data
        CompanyModel saveCompanyModel = this.companyRepository.save(CompanyModelBuilder.createWithEmptyCompanyId());
        String jwt = this.jwtService.generateJwt(saveCompanyModel.getCompanyId().toString());

        RoleModel saveRoleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        EmployeeModel saveEmployeeModel = this.employeeRepository.save(EmployeeModelBuilder.createWithEmptyEmployeeId(saveRoleModel, saveCompanyModel));

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/employee/" + saveEmployeeModel.getEmployeeId().toString() + "/company/" + saveCompanyModel.getCompanyId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        JSONObject responseJSONObject = new JSONObject(response.getContentAsString());
        JSONObject employeeJSONObject = new JSONObject();

        employeeJSONObject.put("employeeId", saveEmployeeModel.getEmployeeId().toString());
        employeeJSONObject.put("firstName", saveEmployeeModel.getFirstName());
        employeeJSONObject.put("lastName", saveEmployeeModel.getLastName());
        employeeJSONObject.put("age", saveEmployeeModel.getAge());
        employeeJSONObject.put("address", saveEmployeeModel.getAddress());
        employeeJSONObject.put("companyId", saveEmployeeModel.getCompanyModel().getCompanyId().toString());
        employeeJSONObject.put("email", saveEmployeeModel.getEmail());
        employeeJSONObject.put("roleId", saveEmployeeModel.getRoleModel().getRoleId().toString());

        assertThat(responseJSONObject.toString().equals(employeeJSONObject.toString())).isEqualTo(true);
    }
    
    @Test
    public void retorna404EMensagemDeErro_EmployeeNaoEncontrada_EmployeeNaoCadastrado() throws Exception {
        // Enviroment data
		CompanyModel saveCompanyModel = this.companyRepository.save(CompanyModelBuilder.createWithEmptyCompanyId());
        String jwt = this.jwtService.generateJwt(saveCompanyModel.getCompanyId().toString());

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/employee/" + UUID.randomUUID().toString() + "/company/" + saveCompanyModel.getCompanyId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Employee not found");
    } 
}




















