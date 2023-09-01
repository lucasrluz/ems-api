package com.emsapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.dtos.SignUpDTORequest;
import com.emsapi.dtos.SignUpDTOResponse;
import com.emsapi.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTORequest signUpDTORequest) {
        try {
            SignUpDTOResponse signUpDTOResponse = this.userService.signUp(signUpDTORequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(signUpDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
