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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import com.emsapi.dtos.user.UpdateUserDTORequest;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.UpdateUserDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiUpdateTests {
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
    public void retornaCodigoDeStatus201EUserId() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        // Test
        UpdateUserDTORequest updateUserDTORequest = UpdateUserDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/user")
            .content(asJsonString(updateUserDTORequest))
            .contentType("application/json")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);
        
        String userId = new JSONObject(response.getContentAsString()).getString("userId");

        Optional<UserModel> findUserModel = this.userRepository.findById(UUID.fromString(userId));

        assertThat(findUserModel.isEmpty()).isEqualTo(false);
        assertThat(findUserModel.get().getUserId()).isEqualTo(userModel.getUserId());
        assertThat(findUserModel.get().getFirstName()).isEqualTo("bar");
        assertThat(findUserModel.get().getLastName()).isEqualTo("foo");
        assertThat(findUserModel.get().getEmail()).isEqualTo("foobar@gmail.com");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_FirstNameInvalido() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        // Test
        UpdateUserDTORequest updateUserDTORequest = UpdateUserDTORequestBuilder.createWithInvalidFirstName();

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/user")
            .content(asJsonString(updateUserDTORequest))
            .contentType("application/json")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("firstName: must not be blank");
    }

    @Test
    public void retornaCodigoDeStatus400EMensagemDeErro_LastNameInvalido() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        // Test
        UpdateUserDTORequest updateUserDTORequest = UpdateUserDTORequestBuilder.createWithInvalidLastName();

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/user")
            .content(asJsonString(updateUserDTORequest))
            .contentType("application/json")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("lastName: must not be blank");
    }
}
