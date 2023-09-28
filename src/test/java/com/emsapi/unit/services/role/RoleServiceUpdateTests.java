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
import com.emsapi.dtos.role.UpdateRoleDTORequest;
import com.emsapi.dtos.role.UpdateRoleDTORespose;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.RoleService;
import com.emsapi.services.util.NameAlreadyRegisteredException;
import com.emsapi.services.util.RoleNotFoundException;
import com.emsapi.util.RoleModelBuilder;
import com.emsapi.util.UpdateRoleDTORequestBuilder;

@ExtendWith(SpringExtension.class)
public class RoleServiceUpdateTests {
    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void retornaRoleId() throws Exception {
        // Mocks
        Optional<RoleModel> roleModelOptionalMock = Optional.of(RoleModelBuilder.createWithRoleId());
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock);

        BDDMockito.when(this.roleRepository.findByName(ArgumentMatchers.any())).thenReturn(Optional.empty());

        roleModelOptionalMock.get().setName("bar");
        BDDMockito.when(this.roleRepository.save(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock.get());

        // Test
        UpdateRoleDTORequest updateRoleDTORequest = UpdateRoleDTORequestBuilder.createWithValidData();

        UpdateRoleDTORespose updateRoleDTORespose = this.roleService.update(
            updateRoleDTORequest,
            roleModelOptionalMock.get().getRoleId().toString()
        );

        assertThat(updateRoleDTORespose.getRoleId()).isEqualTo(roleModelOptionalMock.get().getRoleId().toString());
    }

    @Test
    public void retornaException_RoleNaoEncontrada_RoleNaoCadastrada() throws Exception {
        // Mocks
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Test
        UpdateRoleDTORequest updateRoleDTORequest = UpdateRoleDTORequestBuilder.createWithValidData();

        assertThatExceptionOfType(RoleNotFoundException.class)
        .isThrownBy(() -> this.roleService.update(updateRoleDTORequest, UUID.randomUUID().toString()))
        .withMessage("Role not found");
    }

    @Test
    public void retornaException_NomeJaCadastrado() throws Exception {
        // Mocks
        Optional<RoleModel> roleModelOptionalMock = Optional.of(RoleModelBuilder.createWithRoleId());
        BDDMockito.when(this.roleRepository.findById(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock);

        BDDMockito.when(this.roleRepository.findByName(ArgumentMatchers.any())).thenReturn(roleModelOptionalMock);

        // Test
        UpdateRoleDTORequest updateRoleDTORequest = UpdateRoleDTORequestBuilder.createWithValidData();

        assertThatExceptionOfType(NameAlreadyRegisteredException.class)
        .isThrownBy(() -> this.roleService.update(updateRoleDTORequest, roleModelOptionalMock.get().getRoleId().toString()))
        .withMessage("Name already registered");
    }
}
