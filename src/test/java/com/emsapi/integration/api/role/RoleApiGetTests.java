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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import com.emsapi.models.CompanyModel;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleApiGetTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

	@Autowired
	private CompanyRepository companyRepository;

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
        this.roleRepository.deleteAll();
    }

    @Test
    public void retorna200ERole() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        RoleModel saveRoleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/role/" + saveRoleModel.getRoleId().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        String roleId = new JSONObject(response.getContentAsString()).getString("roleId");
        String name = new JSONObject(response.getContentAsString()).getString("name");
    
        assertThat(roleId).isEqualTo(saveRoleModel.getRoleId().toString());
        assertThat(name).isEqualTo(saveRoleModel.getName());
    }

    @Test
    public void retorna404EMesagemDeErro_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        // Test
        MockHttpServletResponse response = this.mockMvc.perform(
            get("/api/role/" + UUID.randomUUID().toString())
            .header("Authorization", "Bearer " + jwt)
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Role not found");
    }
}
