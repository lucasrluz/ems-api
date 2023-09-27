package com.emsapi.unit.services.role;

import java.util.Optional;
import java.util.UUID;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.emsapi.dtos.role.GetRoleDTOResponse;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.RoleService;
import com.emsapi.services.util.RoleNotFoundException;
import com.emsapi.util.RoleModelBuilder;

@ExtendWith(SpringExtension.class)
public class RoleServiceGetTests {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void retornaRole() throws Exception {
        // Mocks
        Optional<RoleModel> roleModelOptionalMock = Optional.of(RoleModelBuilder.createWithRoleId());
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock);

        // Test
        GetRoleDTOResponse getRoleDTOResponse = this.roleService.get(roleModelOptionalMock.get().getRoleId().toString());

        assertThat(getRoleDTOResponse.getRoleId()).isEqualTo(roleModelOptionalMock.get().getRoleId().toString());
        assertThat(getRoleDTOResponse.getName()).isEqualTo(roleModelOptionalMock.get().getName());
    }

    @Test
    public void retornaException_RoleNaoEncontrada_RoleNaoCadastrada() {
        // Mocks
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Test
        assertThatExceptionOfType(RoleNotFoundException.class)
        .isThrownBy(() -> this.roleService.get(UUID.randomUUID().toString()))
        .withMessage("Role not found");
    }
}
