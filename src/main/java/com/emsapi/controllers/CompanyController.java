package com.emsapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.domains.util.InvalidCompanyDomainException;
import com.emsapi.dtos.authentication.SignInDTORequest;
import com.emsapi.dtos.authentication.SignInDTOResponse;
import com.emsapi.dtos.authentication.SignUpDTORequest;
import com.emsapi.dtos.authentication.SignUpDTOResponse;
import com.emsapi.dtos.company.DeleteCompanyDTOResponse;
import com.emsapi.dtos.company.GetCompanyDTOResponse;
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

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTORequest signUpDTORequest, Authentication authentication) {
        try { 
            SignUpDTOResponse signUpDTOResponse = this.companyService.signUp(signUpDTORequest);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(signUpDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

	@PostMapping("/signin")
	public ResponseEntity<Object> signIn(@RequestBody SignInDTORequest signInDTORequest) {
		try {
			SignInDTOResponse signInDTOResponse = this.companyService.signIn(signInDTORequest);

			return ResponseEntity.status(HttpStatus.CREATED).body(signInDTOResponse);
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}
	}

    @GetMapping("/{companyId}")
    public ResponseEntity<Object> get(@PathVariable String companyId, Authentication authentication) {
        try {
           GetCompanyDTOResponse getCompanyDTOResponse = this.companyService.get(companyId);

            return ResponseEntity.status(HttpStatus.OK).body(getCompanyDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<Object> update(@PathVariable String companyId, @RequestBody UpdateCompanyDTORequest updateCompanyDTORequest, Authentication authentication) {
        try {
            UpdateCompanyDTOResponse updateCompanyDTOResponse = this.companyService.update(updateCompanyDTORequest, companyId);

            return ResponseEntity.status(HttpStatus.OK).body(updateCompanyDTOResponse);
        } catch (CompanyNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (InvalidCompanyDomainException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Object> delete(@PathVariable String companyId, Authentication authentication) {
        try {
            DeleteCompanyDTOResponse deleteCompanyDTOResponse = this.companyService.delete(companyId);

            return ResponseEntity.status(HttpStatus.OK).body(deleteCompanyDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
