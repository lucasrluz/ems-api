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

import com.emsapi.dtos.SignUpDTORequest;
import com.emsapi.dtos.SignUpDTOResponse;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.UserService;
import com.emsapi.services.util.EmailAlreadyRegisteredException;
import com.emsapi.util.SignUpDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class UserServiceSignUpTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaUserId() throws Exception {
        // Mocks
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());

        UserModel userModelSaveMock = UserModelBuilder.createWithUserId();
        BDDMockito.when(this.userRepository.save(ArgumentMatchers.any())).thenReturn(userModelSaveMock);

        // Test
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();
        
        SignUpDTOResponse signUpDTOResponse = this.userService.signUp(signUpDTORequest);
        
        assertThat(signUpDTOResponse.getUserId()).isEqualTo(userModelSaveMock.getUserId().toString());
    }

    @Test
    public void retornaException_EmailJaCadastrado() throws Exception {
        // Mocks
        Optional<UserModel> userModelOptionalFindByEmailMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelOptionalFindByEmailMock);

        // Test
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData(); 

        assertThatExceptionOfType(EmailAlreadyRegisteredException.class)
        .isThrownBy(() -> this.userService.signUp(signUpDTORequest))
        .withMessage("Email already registered");
    }
}
