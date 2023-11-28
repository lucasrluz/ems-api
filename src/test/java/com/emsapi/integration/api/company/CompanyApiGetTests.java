package com.emsapi.integration.api.company;

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

import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;
import com.emsapi.models.CompanyModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyApiGetTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CompanyRepository companyRepository;

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
    }

    @Test
    public void retorna200ECompany() throws Exception {
        // Environment data
        CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());        
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/company/" + companyModel.getCompanyId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        
        String companyId = new JSONObject(response.getContentAsString()).getString("companyId");
        String name = new JSONObject(response.getContentAsString()).getString("name");
        String description = new JSONObject(response.getContentAsString()).getString("description");

        assertThat(companyId).isEqualTo(companyModel.getCompanyId().toString());
        assertThat(name).isEqualTo(companyModel.getName());
        assertThat(description).isEqualTo(companyModel.getDescription());
    }
}
