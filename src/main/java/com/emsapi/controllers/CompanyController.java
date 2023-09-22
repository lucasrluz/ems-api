package com.emsapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.domains.util.InvalidCompanyDomainException;
import com.emsapi.dtos.company.GetAllCompanyDTOResponse;
import com.emsapi.dtos.company.GetCompanyDTOResponse;
import com.emsapi.dtos.company.SaveCompanyDTORequest;
import com.emsapi.dtos.company.SaveCompanyDTOResponse;
import com.emsapi.dtos.company.UpdateCompanyDTORequest;
import com.emsapi.dtos.company.UpdateCompanyDTOResponse;
import com.emsapi.services.CompanyService;
import com.emsapi.services.util.CompanyNotFoundException;

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

    @GetMapping
    public ResponseEntity<Object> getAll(Authentication authentication) {
        String userId = authentication.getName();

        List<GetAllCompanyDTOResponse> getAllCompanyDTOResponse = this.companyService.getAll(userId);

        return ResponseEntity.status(HttpStatus.OK).body(getAllCompanyDTOResponse);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<Object> get(@PathVariable String companyId, Authentication authentication) {
        try {
           String userId = authentication.getName();

           GetCompanyDTOResponse getCompanyDTOResponse = this.companyService.get(companyId, userId);

            return ResponseEntity.status(HttpStatus.OK).body(getCompanyDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<Object> update(@PathVariable String companyId, @RequestBody UpdateCompanyDTORequest updateCompanyDTORequest, Authentication authentication) {
        try {
            String userId = authentication.getName();

            UpdateCompanyDTOResponse updateCompanyDTOResponse = this.companyService.update(updateCompanyDTORequest, companyId, userId);

            return ResponseEntity.status(HttpStatus.OK).body(updateCompanyDTOResponse);
        } catch (CompanyNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (InvalidCompanyDomainException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
