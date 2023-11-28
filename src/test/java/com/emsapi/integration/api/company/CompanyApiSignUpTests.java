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
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.util.SaveCompanyDTORequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyApiSignUpTests {
    @Autowired
    private MockMvc mockMvc;

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
    public void retorna201ECompanyId() throws Exception {
        // Test
        SaveCompanyDTORequest saveCompanyDTORequest = SaveCompanyDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company/signup")
            .contentType("application/json")
            .content(asJsonString(saveCompanyDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        String companyId = new JSONObject(response.getContentAsString()).getString("companyId");
        Optional<CompanyModel> companyModel = this.companyRepository.findById(UUID.fromString(companyId));	

        assertThat(companyModel.isEmpty()).isEqualTo(false);
        assertThat(companyModel.get().getName()).isEqualTo("foo");
        assertThat(companyModel.get().getDescription()).isEqualTo("bar");
    }

    @Test
    public void retorna400EMensagemDeErro_NameInvalido_ValorVazio() throws Exception {
        SaveCompanyDTORequest saveCompanyDTORequest = SaveCompanyDTORequestBuilder.createWithEmptyName();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company/signup")
            .contentType("application/json")
            .content(asJsonString(saveCompanyDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("name: must not be blank");
    }

    @Test
    public void retorna400EMensagemDeErro_DescriptionInvalida_ValorVazio() throws Exception {
        SaveCompanyDTORequest saveCompanyDTORequest = SaveCompanyDTORequestBuilder.createWithEmptyDescription();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company/signup")
            .contentType("application/json")
            .content(asJsonString(saveCompanyDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("description: must not be blank");
    }
}
