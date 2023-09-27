package com.emsapi.unit.services.role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.emsapi.dtos.role.GetAllRoleDTOResponse;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.RoleService;

@ExtendWith(SpringExtension.class)
public class RoleServiceGetAllTests {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void retornaListaComRoles() {
        // Mock
        RoleModel roleModelFooName = new RoleModel("foo");
        RoleModel roleModelBarName = new RoleModel("bar");
        RoleModel roleModelFaoName = new RoleModel("fao");
        
        List<RoleModel> roleModels = new ArrayList<RoleModel>();
        
        roleModels.add(roleModelFooName);
        roleModels.add(roleModelBarName);
        roleModels.add(roleModelFaoName);

        BDDMockito.when(this.roleRepository.findAll()).thenReturn(roleModels);

        // Test
        List<GetAllRoleDTOResponse> getRoleDTOResponses = this.roleService.getAll();

        assertThat(getRoleDTOResponses.get(0).getName()).isEqualTo(roleModelFooName.getName());
        assertThat(getRoleDTOResponses.get(1).getName()).isEqualTo(roleModelBarName.getName());
        assertThat(getRoleDTOResponses.get(2).getName()).isEqualTo(roleModelFaoName.getName());
    }

    @Test
    public void retornaListaVazia() {
        // Mock  
        List<RoleModel> roleModels = new ArrayList<RoleModel>();

        BDDMockito.when(this.roleRepository.findAll()).thenReturn(roleModels);

        // Test
        List<GetAllRoleDTOResponse> getRoleDTOResponses = this.roleService.getAll();

        assertThat(getRoleDTOResponses.isEmpty()).isEqualTo(true);
    }
}
