package com.emsapi.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.emsapi.domains.UserDomain;
import com.emsapi.dtos.authentication.SignUpDTORequest;
import com.emsapi.dtos.authentication.SignUpDTOResponse;
import com.emsapi.dtos.user.DeleteUserDTOResponse;
import com.emsapi.dtos.user.GetUserDTOResponse;
import com.emsapi.dtos.user.UpdateUserDTORequest;
import com.emsapi.dtos.user.UpdateUserDTOResponse;
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

    public GetUserDTOResponse get(String userId) {
        UserModel getUserModel = this.userRepository.findById(UUID.fromString(userId)).get();

        return new GetUserDTOResponse(
            getUserModel.getUserId().toString(),
            getUserModel.getFirstName(),
            getUserModel.getLastName(),
            getUserModel.getEmail()
        );
    }

    public UpdateUserDTOResponse update(UpdateUserDTORequest updateUserDTORequest, String userId) throws Exception {
        UserModel findUserModel = this.userRepository.findById(UUID.fromString(userId)).get();

        UserDomain userDomain = UserDomain.validate(
            updateUserDTORequest.getFirstName(),
            updateUserDTORequest.getLastName(),
            findUserModel.getEmail(),
            findUserModel.getPassword()
        );

        UserModel userModel = new UserModel(
            UUID.fromString(userId),
            userDomain.getFirstName(),
            userDomain.getLastName(),
            userDomain.getEmail(),
            userDomain.getPassword()
        );

        UserModel saveUserModel = this.userRepository.save(userModel);

        return new UpdateUserDTOResponse(saveUserModel.getUserId().toString());
    }

    public DeleteUserDTOResponse delete(String userId) {
        this.userRepository.deleteById(UUID.fromString(userId));

        return new DeleteUserDTOResponse(userId);
    }
}
