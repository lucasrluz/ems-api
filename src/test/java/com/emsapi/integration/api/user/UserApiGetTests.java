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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.json.JSONObject;

import static org.assertj.core.api.Assertions.assertThat;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiGetTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

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
    public void retornaUser() throws Exception {
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());
        
        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());
        
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/user")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        String userId = new JSONObject(response.getContentAsString()).getString("userId");
        String firstName = new JSONObject(response.getContentAsString()).getString("firstName");
        String lastName = new JSONObject(response.getContentAsString()).getString("lastName");
        String email = new JSONObject(response.getContentAsString()).getString("email");

        assertThat(userId).isEqualTo(userModel.getUserId().toString());
        assertThat(firstName).isEqualTo(userModel.getFirstName());
        assertThat(lastName).isEqualTo(userModel.getLastName());
        assertThat(email).isEqualTo(userModel.getEmail());
    }
}
