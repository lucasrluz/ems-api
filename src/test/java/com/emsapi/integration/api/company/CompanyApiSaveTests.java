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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;

import com.emsapi.dtos.company.SaveCompanyDTORequest;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.SaveCompanyDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyApiSaveTests {
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
    public void retornaCodigoDeStatus201ECompanyId() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());
        
        // Test
        SaveCompanyDTORequest saveCompanyDTORequest = SaveCompanyDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveCompanyDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        String companyId = new JSONObject(response.getContentAsString()).getString("companyId");
        Optional<CompanyModel> companyModel = this.companyRepository.findById(UUID.fromString(companyId));

        assertThat(companyModel.isEmpty()).isEqualTo(false);
        assertThat(companyModel.get().getName()).isEqualTo("foo");
        assertThat(companyModel.get().getDescription()).isEqualTo("bar");
        assertThat(companyModel.get().getUserModel().getUserId()).isEqualTo(userModel.getUserId());
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_NameInvalido_ValorVazio() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());
        
        // Test
        SaveCompanyDTORequest saveCompanyDTORequest = SaveCompanyDTORequestBuilder.createWithEmptyName();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveCompanyDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("name: must not be blank");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_DescriptionInvalida_ValorVazio() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());
        
        // Test
        SaveCompanyDTORequest saveCompanyDTORequest = SaveCompanyDTORequestBuilder.createWithEmptyDescription();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveCompanyDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("description: must not be blank");
    }
}
