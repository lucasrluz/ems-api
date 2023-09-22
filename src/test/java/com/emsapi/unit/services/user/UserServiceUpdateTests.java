package com.emsapi.unit.services.user;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.domains.util.InvalidUserDomainException;
import com.emsapi.dtos.user.UpdateUserDTORequest;
import com.emsapi.dtos.user.UpdateUserDTOResponse;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.UserService;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class UserServiceUpdateTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaUserId()throws Exception  {
        UserModel userModel = UserModelBuilder.createWithUserId();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModel);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        userModel.setFirstName("bar");
        userModel.setLastName("foo");
        BDDMockito.when(this.userRepository.save(ArgumentMatchers.any())).thenReturn(userModel);

        UpdateUserDTORequest updateUserDTORequest = new UpdateUserDTORequest(userModel.getFirstName(), userModel.getLastName());

        UpdateUserDTOResponse updateUserDTOResponse = this.userService.update(
            updateUserDTORequest,
            userModelOptionalMock.get().getUserId().toString()
        );

        assertThat(updateUserDTOResponse.getUserId()).isEqualTo(userModelOptionalMock.get().getUserId().toString());
    }

    @Test
    public void retornaException_FirstNameInvalido_ValorVazio()throws Exception  {
        UserModel userModel = UserModelBuilder.createWithUserId();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModel);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        userModel.setFirstName("");
        userModel.setLastName("foo");

        UpdateUserDTORequest updateUserDTORequest = new UpdateUserDTORequest(userModel.getFirstName(), userModel.getLastName());

        assertThatExceptionOfType(InvalidUserDomainException.class)
        .isThrownBy(() -> this.userService.update(
            updateUserDTORequest,
            userModelOptionalMock.get().getUserId().toString()
        ))
        .withMessage("firstName: must not be blank");

    }

    @Test
    public void retornaException_LastNameInvalido_ValorVazio()throws Exception  {
        UserModel userModel = UserModelBuilder.createWithUserId();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModel);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        userModel.setFirstName("bar");
        userModel.setLastName("");

        UpdateUserDTORequest updateUserDTORequest = new UpdateUserDTORequest(userModel.getFirstName(), userModel.getLastName());

        assertThatExceptionOfType(InvalidUserDomainException.class)
        .isThrownBy(() -> this.userService.update(
            updateUserDTORequest,
            userModelOptionalMock.get().getUserId().toString()
        ))
        .withMessage("lastName: must not be blank");

    }
}
