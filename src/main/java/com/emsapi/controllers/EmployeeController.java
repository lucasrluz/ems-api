package com.emsapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.domains.util.InvalidEmployeeDomainException;
import com.emsapi.dtos.employee.GetAllEmployeeDTOResponse;
import com.emsapi.dtos.employee.SaveEmployeeDTORequest;
import com.emsapi.dtos.employee.SaveEmployeeDTOResponse;
import com.emsapi.services.EmployeeService;
import com.emsapi.services.util.CompanyNotFoundException;
import com.emsapi.services.util.RoleNotFoundException;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SaveEmployeeDTORequest saveEmployeeDTORequest) {
        try {
            SaveEmployeeDTOResponse saveEmployeeDTOResponse = this.employeeService.save(saveEmployeeDTORequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(saveEmployeeDTOResponse);
        } catch (RoleNotFoundException | CompanyNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (InvalidEmployeeDomainException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Object> getAll(@PathVariable String companyId, Authentication authentication) {
        try {
            String userId = authentication.getName();

            List<GetAllEmployeeDTOResponse> getAllEmployeeDTOResponses = this.employeeService.getAll(companyId, userId);

            return ResponseEntity.status(HttpStatus.OK).body(getAllEmployeeDTOResponses);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
