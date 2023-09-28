package com.emsapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.stereotype.Service;

import com.emsapi.domains.RoleDomain;
import com.emsapi.domains.util.InvalidRoleDomainException;
import com.emsapi.dtos.company.DeleteRoleDTOResponse;
import com.emsapi.dtos.role.GetAllRoleDTOResponse;
import com.emsapi.dtos.role.GetRoleDTOResponse;
import com.emsapi.dtos.role.SaveRoleDTORequest;
import com.emsapi.dtos.role.SaveRoleDTOResponse;
import com.emsapi.dtos.role.UpdateRoleDTORequest;
import com.emsapi.dtos.role.UpdateRoleDTORespose;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.util.NameAlreadyRegisteredException;
import com.emsapi.services.util.RoleNotFoundException;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public SaveRoleDTOResponse save(SaveRoleDTORequest saveRoleDTORequest) throws InvalidRoleDomainException, NameAlreadyRegisteredException {
        RoleDomain roleDomain = RoleDomain.validate(saveRoleDTORequest.getName());

        Optional<RoleModel> findRoleModel = this.roleRepository.findByName(roleDomain.getName());

        if (!findRoleModel.isEmpty()) {
            throw new NameAlreadyRegisteredException();
        }

        RoleModel roleModel = new RoleModel(roleDomain.getName());

        RoleModel saveRoleModel = this.roleRepository.save(roleModel);

        return new SaveRoleDTOResponse(saveRoleModel.getRoleId().toString());
    }

    public List<GetAllRoleDTOResponse> getAll() {
        List<RoleModel> roleModels = this.roleRepository.findAll();

        List<GetAllRoleDTOResponse> getAllRoleDTOResponses = new ArrayList<GetAllRoleDTOResponse>();

        roleModels.forEach((element) -> {
            GetAllRoleDTOResponse getRoleDTOResponse = new GetAllRoleDTOResponse(element.getRoleId().toString(), element.getName());
            getAllRoleDTOResponses.add(getRoleDTOResponse);
        });

        return getAllRoleDTOResponses;
    }

    public GetRoleDTOResponse get(String roleId) throws RoleNotFoundException {
        Optional<RoleModel> findRoleByRoleId = this.roleRepository.findById(UUID.fromString(roleId));

        if (findRoleByRoleId.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return new GetRoleDTOResponse(
            findRoleByRoleId.get().getRoleId().toString(),
            findRoleByRoleId.get().getName()
        );
    }

    public UpdateRoleDTORespose update(UpdateRoleDTORequest updateRoleDTORequest, String roleId) throws InvalidRoleDomainException, RoleNotFoundException, NameAlreadyRegisteredException {
        RoleDomain roleDomain = RoleDomain.validate(updateRoleDTORequest.getName());

        Optional<RoleModel> findRoleModelByRoleId = this.roleRepository.findById(UUID.fromString(roleId));

        if (findRoleModelByRoleId.isEmpty()) {
            throw new RoleNotFoundException();
        }

        Optional<RoleModel> findRoleModelByName = this.roleRepository.findByName(roleDomain.getName());

        if (!findRoleModelByName.isEmpty()) {
            throw new NameAlreadyRegisteredException();
        }

        RoleModel roleModel = new RoleModel(UUID.fromString(roleId), roleDomain.getName());

        RoleModel updateRoleModel = this.roleRepository.save(roleModel);

        return new UpdateRoleDTORespose(updateRoleModel.getRoleId().toString());
    }

    public DeleteRoleDTOResponse delete(String roleId) throws RoleNotFoundException {
        Optional<RoleModel> findRoleModelByRoleId = this.roleRepository.findById(UUID.fromString(roleId));

        if (findRoleModelByRoleId.isEmpty()) {
            throw new RoleNotFoundException();
        }

        this.roleRepository.deleteById(findRoleModelByRoleId.get().getRoleId());

        return new DeleteRoleDTOResponse(findRoleModelByRoleId.get().getRoleId().toString());
    }
}
