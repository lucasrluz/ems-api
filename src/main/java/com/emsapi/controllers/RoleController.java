package com.emsapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.domains.util.InvalidRoleDomainException;
import com.emsapi.dtos.company.DeleteRoleDTOResponse;
import com.emsapi.dtos.role.GetAllRoleDTOResponse;
import com.emsapi.dtos.role.GetRoleDTOResponse;
import com.emsapi.dtos.role.SaveRoleDTORequest;
import com.emsapi.dtos.role.SaveRoleDTOResponse;
import com.emsapi.dtos.role.UpdateRoleDTORequest;
import com.emsapi.dtos.role.UpdateRoleDTORespose;
import com.emsapi.services.RoleService;
import com.emsapi.services.util.NameAlreadyRegisteredException;
import com.emsapi.services.util.RoleNotFoundException;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SaveRoleDTORequest saveRoleDTORequest) {
        try {
            SaveRoleDTOResponse saveRoleDTOResponse = this.roleService.save(saveRoleDTORequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(saveRoleDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<GetAllRoleDTOResponse> getAllRoleDTOResponses = this.roleService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(getAllRoleDTOResponses);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Object> get(@PathVariable String roleId) {
        try {
            GetRoleDTOResponse getRoleDTOResponse = this.roleService.get(roleId);

            return ResponseEntity.status(HttpStatus.OK).body(getRoleDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Object> update(@RequestBody UpdateRoleDTORequest updateRoleDTORequest, @PathVariable String roleId) {
        try {
            UpdateRoleDTORespose updateRoleDTORespose = this.roleService.update(updateRoleDTORequest, roleId);

            return ResponseEntity.status(HttpStatus.OK).body(updateRoleDTORespose);
        } catch (RoleNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (InvalidRoleDomainException | NameAlreadyRegisteredException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Object> delete(@PathVariable String roleId) {
        try {
            DeleteRoleDTOResponse deleteRoleDTOResponse = this.roleService.delete(roleId);

            return ResponseEntity.status(HttpStatus.OK).body(deleteRoleDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
