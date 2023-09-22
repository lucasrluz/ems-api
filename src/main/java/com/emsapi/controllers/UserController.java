package com.emsapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emsapi.dtos.authentication.SignUpDTORequest;
import com.emsapi.dtos.authentication.SignUpDTOResponse;
import com.emsapi.dtos.user.DeleteUserDTOResponse;
import com.emsapi.dtos.user.GetUserDTOResponse;
import com.emsapi.dtos.user.UpdateUserDTORequest;
import com.emsapi.dtos.user.UpdateUserDTOResponse;
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

    @GetMapping
    public ResponseEntity<Object> get(Authentication authentication) {
        String userId = authentication.getName();

        GetUserDTOResponse getUserDTOResponse = this.userService.get(userId);

        return ResponseEntity.status(HttpStatus.OK).body(getUserDTOResponse);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody UpdateUserDTORequest updateUserDTORequest, Authentication authentication) {
        try {
            String userId = authentication.getName();

            UpdateUserDTOResponse updateUserDTOResponse = this.userService.update(updateUserDTORequest, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(updateUserDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(Authentication authentication) {
        String userId = authentication.getName();

        DeleteUserDTOResponse deleteUserDTOResponse = this.userService.delete(userId);

        return ResponseEntity.status(HttpStatus.OK).body(deleteUserDTOResponse);
    }
}
