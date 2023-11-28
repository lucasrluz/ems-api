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

import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;

import com.emsapi.dtos.authentication.SignInDTORequest;
import com.emsapi.models.CompanyModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.SignInDTORequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyApiSignInTests {
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
    public void retorna201EJWT() throws Exception {
        // Test
		CompanyModel companyModel = CompanyModelBuilder.createWithEmptyCompanyId();
		CompanyModel saveCompanyModel = this.companyRepository.save(companyModel);

		SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company/signin")
            .contentType("application/json")
            .content(asJsonString(signInDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

		String jwt = new JSONObject(response.getContentAsString()).getString("jwt");

		String companyId = this.jwtService.validateJwt(jwt);

        assertThat(companyId).isEqualTo(saveCompanyModel.getCompanyId().toString());
    }

    @Test
    public void retorna400EMensagemDeErro_EmailNaoCadastrado() throws Exception {
        // Test
		SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company/signin")
            .contentType("application/json")
            .content(asJsonString(signInDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Email or password invalid");
    }

    @Test
    public void retorna400EMensagemDeErro_PasswordInvalida() throws Exception {
        // Test
		CompanyModel companyModel = CompanyModelBuilder.createWithEmptyCompanyId();
		this.companyRepository.save(companyModel);

		SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();
		signInDTORequest.setPassword("456");

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/company/signin")
            .contentType("application/json")
            .content(asJsonString(signInDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Email or password invalid");
    }
}
