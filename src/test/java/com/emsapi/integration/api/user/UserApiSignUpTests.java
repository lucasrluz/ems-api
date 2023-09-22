package com.emsapi.integration.api.user;

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

import com.emsapi.dtos.authentication.SignUpDTORequest;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.util.SignUpDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiSignUpTests {
    @Autowired
    private MockMvc mockMvc;

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
        this.userRepository.deleteAll();
    }

    @Test
    public void retornaCodigoDeStatus201EUserId() throws Exception {
        // Request
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/user/signup")
            .contentType("application/json")
            .content(this.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        // Test
        assertThat(response.getStatus()).isEqualTo(201);

        String userId = new JSONObject(response.getContentAsString()).getString("userId");
        
        Optional<UserModel> findUserModel = this.userRepository.findById(UUID.fromString(userId));

        assertThat(findUserModel.isEmpty()).isEqualTo(false);
        assertThat(findUserModel.get().getUserId().toString()).isEqualTo(userId);
        assertThat(findUserModel.get().getFirstName()).isEqualTo(signUpDTORequest.getFirstName());
        assertThat(findUserModel.get().getLastName()).isEqualTo(signUpDTORequest.getLastName());
        assertThat(findUserModel.get().getEmail()).isEqualTo(signUpDTORequest.getEmail());
        
        boolean passwordVerify = BCrypt.verifyer().verify(
            signUpDTORequest.getPassword().toCharArray(),
            findUserModel.get().getPassword()
        ).verified;

        assertThat(passwordVerify).isEqualTo(true);
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_EmailJaCadastrado() throws Exception {
        //
        this.userRepository.save(UserModelBuilder.createWithEmptyUserId());
        
        // Request
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/user/signup")
            .contentType("application/json")
            .content(this.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        // Test
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Email already registered");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_FistNameInvalido_ValorVazio() throws Exception {
        // Request
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmptyFirstName();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/user/signup")
            .contentType("application/json")
            .content(this.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        // Test
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("firstName: must not be blank");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_LastNameInvalido_ValorVazio() throws Exception {
        // Request
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmptyLastName();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/user/signup")
            .contentType("application/json")
            .content(this.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        // Test
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("lastName: must not be blank");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_EmailInvalido_ValorVazio() throws Exception {
        // Request
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmptyEmail();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/user/signup")
            .contentType("application/json")
            .content(this.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        // Test
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("email: must not be blank");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_EmailInvalido_FormatoInvalido() throws Exception {
        // Request
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithInvalidEmailFormat();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/user/signup")
            .contentType("application/json")
            .content(this.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        // Test
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("email: must be a well-formed email address");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_PasswordInvalida_ValorVazio() throws Exception {
        // Request
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmpyPassword();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/user/signup")
            .contentType("application/json")
            .content(this.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        // Test
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("password: must not be blank");
    }
}
