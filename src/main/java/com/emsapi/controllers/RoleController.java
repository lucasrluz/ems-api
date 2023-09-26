package com.emsapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.dtos.role.SaveRoleDTORequest;
import com.emsapi.dtos.role.SaveRoleDTOResponse;
import com.emsapi.services.RoleService;

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
}
