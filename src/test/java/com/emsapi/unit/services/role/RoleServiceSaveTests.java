package com.emsapi.unit.services.role;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.role.SaveRoleDTORequest;
import com.emsapi.dtos.role.SaveRoleDTOResponse;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.RoleService;
import com.emsapi.services.util.NameAlreadyRegisteredException;
import com.emsapi.util.RoleModelBuilder;

@ExtendWith(SpringExtension.class)
public class RoleServiceSaveTests {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void retornaRoleId() throws Exception {
        // Mocks
        BDDMockito.when(this.roleRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());
        
        RoleModel roleModelMock = RoleModelBuilder.createWithRoleId();
        BDDMockito.when(this.roleRepository.save(ArgumentMatchers.any())).thenReturn(roleModelMock);

        // Test
        SaveRoleDTORequest saveRoleDTORequest = new SaveRoleDTORequest("foo");

        SaveRoleDTOResponse saveRoleDTOResponse = this.roleService.save(saveRoleDTORequest);

        assertThat(saveRoleDTOResponse.getRoleId()).isEqualTo(roleModelMock.getRoleId().toString());
    }

    @Test
    public void retornaException_NameJaCadastrado() throws Exception {
        // Mocks
        Optional<RoleModel> roleModelMock = Optional.of(RoleModelBuilder.createWithRoleId());
        BDDMockito.when(this.roleRepository.findByName(ArgumentMatchers.any())).thenReturn(roleModelMock);
        
        // Test
        SaveRoleDTORequest saveRoleDTORequest = new SaveRoleDTORequest("foo");

        assertThatExceptionOfType(NameAlreadyRegisteredException.class)
        .isThrownBy(() -> this.roleService.save(saveRoleDTORequest))
        .withMessage("Name already registered");
    }
}
