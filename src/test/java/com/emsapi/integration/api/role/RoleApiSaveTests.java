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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.json.JSONObject;

import com.emsapi.dtos.role.SaveRoleDTORequest;
import com.emsapi.models.CompanyModel;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.CompanyRepository;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.JwtService;
import com.emsapi.util.CompanyModelBuilder;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.SaveRoleDTORequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleApiSaveTests {
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
    public void retorna201ERoleId() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        // Test
        SaveRoleDTORequest saveRoleDTORequest = SaveRoleDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/role")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveRoleDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        String roleId = new JSONObject(response.getContentAsString()).getString("roleId");
        Optional<RoleModel> findRoleModelById = this.roleRepository.findById(UUID.fromString(roleId));

        assertThat(findRoleModelById.isEmpty()).isEqualTo(false);
        assertThat(findRoleModelById.get().getName()).isEqualTo("foo");
    }

    @Test
    public void retorna400EMensagemDeErro_NameJaCadastrado() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        this.roleRepository.save(RoleModelBuilder.createWithEmptyRoleId());

        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        // Test
        SaveRoleDTORequest saveRoleDTORequest = SaveRoleDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/role")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveRoleDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Name already registered");
    }

    @Test
    public void retorna400EMensagemDeErro_NameInvalido_ValorVazio() throws Exception {
        // Environment data
		CompanyModel companyModel = this.companyRepository.save(CompanyModelBuilder.createWithCompanyId());
        String jwt = this.jwtService.generateJwt(companyModel.getCompanyId().toString());

        // Test
        SaveRoleDTORequest saveRoleDTORequest = SaveRoleDTORequestBuilder.createWithEmptyName();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/role")
            .header("Authorization", "Bearer " + jwt)
            .contentType("application/json")
            .content(asJsonString(saveRoleDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("name: must not be blank");
    }
}
