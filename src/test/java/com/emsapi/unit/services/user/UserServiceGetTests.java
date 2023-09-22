package com.emsapi.unit.services.user;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.user.GetUserDTOResponse;
import com.emsapi.models.UserModel;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.UserService;
import com.emsapi.util.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class UserServiceGetTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaInformacoesDoUsuario() throws Exception {
        Optional<UserModel> userModelOptionalMock = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        GetUserDTOResponse getUserDTOResponse = this.userService.get(userModelOptionalMock.get().getUserId().toString());

        assertThat(getUserDTOResponse.getUserId()).isEqualTo(userModelOptionalMock.get().getUserId().toString());
        assertThat(getUserDTOResponse.getFirstName()).isEqualTo(userModelOptionalMock.get().getFirstName());
        assertThat(getUserDTOResponse.getLastName()).isEqualTo(userModelOptionalMock.get().getLastName());
        assertThat(getUserDTOResponse.getEmail()).isEqualTo(userModelOptionalMock.get().getEmail());
    }
}
