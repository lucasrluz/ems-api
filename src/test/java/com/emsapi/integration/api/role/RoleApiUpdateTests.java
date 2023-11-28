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
import static org.assertj.core.api.Assertions.assertThat;
import org.json.JSONObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;
import java.util.UUID;

import com.emsapi.dtos.role.UpdateRoleDTORequest;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UpdateRoleDTORequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleApiUpdateTests {
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
    public void retorna200ERoleId() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        RoleModel saveRoleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        // Test
        UpdateRoleDTORequest updateRoleDTORequest = UpdateRoleDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/role/" + saveRoleModel.getRoleId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateRoleDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);

        String roleId = new JSONObject(response.getContentAsString()).getString("roleId");
        
        Optional<RoleModel> findRoleModel = this.roleRepository.findById(UUID.fromString(roleId));

        assertThat(findRoleModel.isEmpty()).isEqualTo(false);
        assertThat(findRoleModel.get().getName()).isEqualTo(updateRoleDTORequest.getName());
    }

    @Test
    public void retorna404EMensagemDeErro_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        // Test
        UpdateRoleDTORequest updateRoleDTORequest = UpdateRoleDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/role/" + UUID.randomUUID().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateRoleDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getContentAsString()).isEqualTo("Role not found");
    }

    @Test
    public void retorna404EMensagemDeErro_NameJaCadastrado() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        RoleModel saveRoleModel = this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        RoleModel roleModel = RoleModelBuilder.createWithEmptyRoleId();
        roleModel.setName("bar");
        this.roleRepository.save(roleModel);

        // Test
        UpdateRoleDTORequest updateRoleDTORequest = UpdateRoleDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            put("/api/role/" + saveRoleModel.getRoleId().toString())
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(updateRoleDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Name already registered");
    }
}
