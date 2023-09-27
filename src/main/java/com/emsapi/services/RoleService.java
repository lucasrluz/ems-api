package com.emsapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emsapi.domains.RoleDomain;
import com.emsapi.domains.util.InvalidRoleDomainException;
import com.emsapi.dtos.role.GetAllRoleDTOResponse;
import com.emsapi.dtos.role.SaveRoleDTORequest;
import com.emsapi.dtos.role.SaveRoleDTOResponse;
import com.emsapi.models.RoleModel;
import com.emsapi.repositories.RoleRepository;
import com.emsapi.services.util.NameAlreadyRegisteredException;

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
}
