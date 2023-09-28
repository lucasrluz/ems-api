package com.emsapi.integration.api.role;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleApiDeleteTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        this.roleRepository.deleteAll();
    }

    @Test
    public void retorna200ERoleId() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        RoleModel saveRoleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            delete("/api/role/" + saveRoleModel.getRoleId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        String roleId = new JSONObject(response.getContentAsString()).getString("roleId");
        Optional<RoleModel> findRoleModel = this.roleRepository.findById(UUID.fromString(roleId));

        assertThat(findRoleModel.isEmpty()).isEqualTo(true);
    }

    @Test
    public void retorna404EMensagemDeErro_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            delete("/api/role/" + UUID.randomUUID().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Role not found");
    }
}
