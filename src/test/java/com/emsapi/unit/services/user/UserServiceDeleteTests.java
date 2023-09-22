package com.emsapi.unit.services.user;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.emsapi.dtos.user.DeleteUserDTOResponse;
import com.emsapi.repositories.UserRepository;
import com.emsapi.services.UserService;

@ExtendWith(SpringExtension.class)
public class UserServiceDeleteTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaUserId() {
        String userId = UUID.randomUUID().toString();

        DeleteUserDTOResponse deleteUserDTOResponse = this.userService.delete(userId);

        assertThat(deleteUserDTOResponse.getUserId()).isEqualTo(userId);
    }
}
