package com.emsapi.integration.api.authentication;

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

import static org.assertj.core.api.Assertions.assertThat;

import com.emsapi.dtos.authentication.SignInDTORequest;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.SignInDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationApiSignInTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

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
        this.userRepository.deleteAll();
    }

    @Test
    public void retorna201EJWT() throws Exception {
        // Environment data
        UserModel userModel = UserModelBuilder.createWithEmptyUserId();
        UserModel saveUserModel = this.userRepository.save(userModel);

        // Test
        SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signin")
            .contentType("application/json")
            .content(asJsonString(signInDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        String jwt = new JSONObject(response.getContentAsString()).getString("jwt");

        assertThat(this.jwtService.validateJwt(jwt)).isEqualTo(saveUserModel.getUserId().toString());
    }

    @Test
    public void retorna400EMensagemDeErro_EmailNaoCadastrado() throws Exception {
        // Test
        SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signin")
            .contentType("application/json")
            .content(asJsonString(signInDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Email or password invalid");
    }

    @Test
    public void retorna400EMensagemDeErro_PasswordInvalida() throws Exception {
        // Environment data
        UserModel userModel = UserModelBuilder.createWithEmptyUserId();
        this.userRepository.save(userModel);

        // Test
        SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();
        signInDTORequest.setPassword("456");

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signin")
            .contentType("application/json")
            .content(asJsonString(signInDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Email or password invalid");
    }
}
