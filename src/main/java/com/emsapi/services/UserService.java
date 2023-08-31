package com.emsapi.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.emsapi.domains.UserDomain;
import com.emsapi.dtos.SignUpDTORequest;
import com.emsapi.dtos.SignUpDTOResponse;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.util.EmailAlreadyRegisteredException;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignUpDTOResponse signUp(SignUpDTORequest signUpDTORequest) throws Exception {
        UserDomain userDomain = UserDomain.validate(
            signUpDTORequest.getFirstName(),
            signUpDTORequest.getLastName(),
            signUpDTORequest.getEmail(),
            signUpDTORequest.getPassword()
        );

        Optional<UserModel> findUserModel = this.userRepository.findByEmail(userDomain.getEmail());

        if (!findUserModel.isEmpty()) {
            throw new EmailAlreadyRegisteredException();
        }

        String hashPassword = BCrypt.withDefaults().hashToString(12, userDomain.getPassword().toCharArray());

        UserModel userModel = new UserModel(
            userDomain.getFirstName(),
            userDomain.getLastName(),
            userDomain.getEmail(),
            hashPassword
        );

        UserModel saveUserModel = this.userRepository.save(userModel);

        return new SignUpDTOResponse(saveUserModel.getUserId().toString());
    }
}