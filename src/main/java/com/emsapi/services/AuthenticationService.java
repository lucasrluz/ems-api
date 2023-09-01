package com.emsapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emsapi.dtos.SignInDTORequest;
import com.emsapi.dtos.SignInDTOResponse;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.util.EmailOrPasswordInvalidException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class AuthenticationService {
    private JwtService jwtService;
    private UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public SignInDTOResponse signIn(SignInDTORequest signInDTORequest) throws Exception {
        Optional<UserModel> findUserModel = this.userRepository.findByEmail(signInDTORequest.getEmail());

        if (findUserModel.isEmpty()) {
            throw new EmailOrPasswordInvalidException();
        }

        boolean validPassword = BCrypt.verifyer().verify(
            signInDTORequest.getPassword().toCharArray(),
            findUserModel.get().getPassword()
        ).verified;

        if (!validPassword) {
            throw new EmailOrPasswordInvalidException();
        }

        String jwt = this.jwtService.generateJwt(findUserModel.get().getUserId().toString());

        return new SignInDTOResponse(jwt);
    }
}
