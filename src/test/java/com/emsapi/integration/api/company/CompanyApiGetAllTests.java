package com.emsapi.integration.api.company;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyApiGetAllTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

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
        this.companyRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void retorna200EListaComCompanys() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        List<CompanyModel> companyModels = new ArrayList<CompanyModel>();

        companyModels.add(this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel)));
        companyModels.add(this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel)));
        companyModels.add(this.companyRepository.save(CompanyModelBuilder.createWithCompanyId(userModel)));        

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/company")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        
        JSONArray responseJsonArray = new JSONArray(response.getContentAsString());

        String responseString = "[{\"companyId\":\"" + companyModels.get(0).getCompanyId().toString() + "\",\"name\":\"foo\",\"description\":\"bar\"},{\"companyId\":\"" + companyModels.get(1).getCompanyId().toString() + "\",\"name\":\"foo\",\"description\":\"bar\"},{\"companyId\":\"" + companyModels.get(2).getCompanyId().toString() + "\",\"name\":\"foo\",\"description\":\"bar\"}]";

        assertThat(responseJsonArray.toString()).isEqualTo(responseString);
    }

    @Test
    public void retorna200EListaVazia_NenhumaCompanyCadastrada() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString()); 

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/company")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        
        JSONArray responseJsonArray = new JSONArray(response.getContentAsString());

        assertThat(responseJsonArray.toString()).isEqualTo("[]");
    }
}
