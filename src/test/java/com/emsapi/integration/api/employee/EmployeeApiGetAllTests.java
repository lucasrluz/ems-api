package com.emsapi.integration.api.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import org.json.JSONArray;
import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeApiGetAllTests {
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
    public void retorna200EListaComEmployees() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel));

        RoleModel roleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        List<EmployeeModel> employeeModels = EmployeeModelBuilder.createListWithEmptyEmployeeId(roleModel, companyModel);
        
        List<EmployeeModel> saveEmployeeModels = new ArrayList<EmployeeModel>();
        saveEmployeeModels.add(this.employeeRepository.save(employeeModels.get(0)));
        saveEmployeeModels.add(this.employeeRepository.save(employeeModels.get(1)));
        saveEmployeeModels.add(this.employeeRepository.save(employeeModels.get(2)));
        
        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/employee/" + companyModel.getCompanyId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        JSONArray responseJSONArray = new JSONArray(response.getContentAsString());

        JSONArray JSONArray = new JSONArray();
        
        JSONObject firstJSONObject = new JSONObject();
        JSONObject secondJSONObject = new JSONObject();
        JSONObject thirdJSONObject = new JSONObject();
        
        firstJSONObject.put("firstName", saveEmployeeModels.get(0).getFirstName());
        firstJSONObject.put("lastName", saveEmployeeModels.get(0).getLastName());
        firstJSONObject.put("age", saveEmployeeModels.get(0).getAge());
        firstJSONObject.put("address", saveEmployeeModels.get(0).getAddress());
        firstJSONObject.put("companyId", saveEmployeeModels.get(0).getCompanyModel().getCompanyId().toString());
        firstJSONObject.put("email", saveEmployeeModels.get(0).getEmail());
        firstJSONObject.put("roleId", saveEmployeeModels.get(0).getRoleModel().getRoleId().toString());

        secondJSONObject.put("firstName", saveEmployeeModels.get(1).getFirstName());
        secondJSONObject.put("lastName", saveEmployeeModels.get(1).getLastName());
        secondJSONObject.put("age", saveEmployeeModels.get(1).getAge());
        secondJSONObject.put("address", saveEmployeeModels.get(1).getAddress());
        secondJSONObject.put("companyId", saveEmployeeModels.get(1).getCompanyModel().getCompanyId().toString());
        secondJSONObject.put("email", saveEmployeeModels.get(1).getEmail());
        secondJSONObject.put("roleId", saveEmployeeModels.get(1).getRoleModel().getRoleId().toString());

        thirdJSONObject.put("firstName", saveEmployeeModels.get(2).getFirstName());
        thirdJSONObject.put("lastName", saveEmployeeModels.get(2).getLastName());
        thirdJSONObject.put("age", saveEmployeeModels.get(2).getAge());
        thirdJSONObject.put("address", saveEmployeeModels.get(2).getAddress());
        thirdJSONObject.put("companyId", saveEmployeeModels.get(2).getCompanyModel().getCompanyId().toString());
        thirdJSONObject.put("email", saveEmployeeModels.get(2).getEmail());
        thirdJSONObject.put("roleId", saveEmployeeModels.get(2).getRoleModel().getRoleId().toString());

        JSONArray.put(firstJSONObject);
        JSONArray.put(secondJSONObject);
        JSONArray.put(thirdJSONObject);

        assertThat(responseJSONArray.toString().equals(JSONArray.toString())).isEqualTo(true);
    }

    @Test
    public void retorna404EMensagemDeErro_CompanyNaoEncontrada_CompanyNaoCadastrada() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());
        
        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/employee/" + UUID.randomUUID().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Company not found");
    }

    @Test
    public void retorna404EMensagemDeErro_CompanyNaoEncontrada_UserIdDiferenteDoInformado() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        UserModel userModelForCompany = UserModelBuilder.createWithEmptyUserId();
        userModelForCompany.setEmail("barfoo@gmail.com");
        this.userRepository.save(userModelForCompany);
        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModelForCompany));
        
        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/employee/" + companyModel.getCompanyId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Company not found");
    }
}
