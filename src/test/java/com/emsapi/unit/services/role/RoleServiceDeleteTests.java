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
import com.emsapi.dtos.company.DeleteRoleDTOResponse;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.RoleService;
import com.emsapi.services.util.RoleNotFoundException;
import com.emsapi.util.RoleModelBuilder;

@ExtendWith(SpringExtension.class)
public class RoleServiceDeleteTests {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void retornaRoleId() throws Exception {
        // Mock
        Optional<RoleModel> roleModelOptionalMock = Optional.of(RoleModelBuilder.createWithRoleId());
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock);

        // Test
        DeleteRoleDTOResponse deleteRoleDTOResponse = this.roleService.delete(roleModelOptionalMock.get().getRoleId().toString());
    
        assertThat(deleteRoleDTOResponse.getRoleId()).isEqualTo(roleModelOptionalMock.get().getRoleId().toString());
    }

    @Test
    public void retornaException_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Mock
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Test    
        assertThatExceptionOfType(RoleNotFoundException.class)
        .isThrownBy(() -> this.roleService.delete(UUID.randomUUID().toString()))
        .withMessage("Role not found");
    }
}
