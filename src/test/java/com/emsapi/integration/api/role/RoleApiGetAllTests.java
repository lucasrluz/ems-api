package com.emsapi.integration.api.role;

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
import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONArray;
import org.json.JSONObject;
import com.emsapi.models.RoleModel;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleApiGetAllTests {
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
    public void retorna200EListaComRoles() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());

        RoleModel roleModelFooName = new RoleModel("foo");
        RoleModel roleModelBarName = new RoleModel("bar");
        RoleModel roleModelFaoName = new RoleModel("fao");

        this.roleRepository.save(roleModelFooName);
        this.roleRepository.save(roleModelBarName);
        this.roleRepository.save(roleModelFaoName);
        
        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/role")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        JSONArray responseJsonArray = new JSONArray(response.getContentAsString());

        String responseString = "[{\"roleId\":\"" + roleModelFooName.getRoleId().toString() + "\",\"name\":\"foo\"},{\"roleId\":\"" + roleModelBarName.getRoleId().toString() + "\",\"name\":\"bar\"},{\"roleId\":\"" + roleModelFaoName.getRoleId().toString() + "\",\"name\":\"fao\"}]";
    
        assertThat(responseJsonArray.toString()).isEqualTo(responseString);
    }

    @Test
    public void retorna200EListaVazia() throws Exception {
        // Environment data
        UserModel userModel = this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        String jwt = this.jwtService.generateJwt(userModel.getUserId().toString());
        
        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/role")
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
