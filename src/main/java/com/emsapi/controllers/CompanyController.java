package com.emsapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.emsapi.dtos.company.SaveCompanyDTORequest;
import com.emsapi.dtos.company.SaveCompanyDTOResponse;
import com.emsapi.services.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody SaveCompanyDTORequest saveCompanyDTORequest, Authentication authentication) {
        try {
            String userId = authentication.getName();
            
            SaveCompanyDTOResponse saveCompanyDTOResponse = this.companyService.save(saveCompanyDTORequest, userId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(saveCompanyDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
