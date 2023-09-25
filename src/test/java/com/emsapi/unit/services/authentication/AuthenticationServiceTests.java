package com.emsapi.unit.services.authentication;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.emsapi.dtos.authentication.SignInDTORequest;
import com.emsapi.dtos.authentication.SignInDTOResponse;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.AuthenticationService;
import com.emsapi.services.JwtService;
import com.emsapi.services.util.EmailOrPasswordInvalidException;
import com.emsapi.util.SignInDTORequestBuilder;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthenticationServiceTests {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaJWT() throws Exception {
        // Mock
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        BDDMockito.when(this.jwtService.generateJwt(ArgumentMatchers.any())).thenReturn("fake-jwt");

        // Test
        SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

        SignInDTOResponse signInDTOResponse = this.authenticationService.signIn(signInDTORequest);
        
        assertThat(signInDTOResponse.getJwt()).isEqualTo("fake-jwt");
    }

    @Test
    public void retornaException_EmailInvalido_EmailNaoCadastrado() throws Exception {
        // Mock
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // Test
        SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();

        assertThatExceptionOfType(EmailOrPasswordInvalidException.class)
        .isThrownBy(() -> this.authenticationService.signIn(signInDTORequest))
        .withMessage("Email or password invalid");
    }

    @Test
    public void retornaException_PasswordInvalida_PasswordsDiferentes() throws Exception {
        // Mock
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        // Test
        SignInDTORequest signInDTORequest = SignInDTORequestBuilder.createWithValidData();
        signInDTORequest.setPassword("456");

        assertThatExceptionOfType(EmailOrPasswordInvalidException.class)
        .isThrownBy(() -> this.authenticationService.signIn(signInDTORequest))
        .withMessage("Email or password invalid");
    }
}
