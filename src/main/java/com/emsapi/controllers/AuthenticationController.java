package com.emsapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.dtos.authentication.SignInDTORequest;
import com.emsapi.dtos.authentication.SignInDTOResponse;
import com.emsapi.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody SignInDTORequest signInDTORequest) {
        try {
            SignInDTOResponse signInDTOResponse = this.authenticationService.signIn(signInDTORequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(signInDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
