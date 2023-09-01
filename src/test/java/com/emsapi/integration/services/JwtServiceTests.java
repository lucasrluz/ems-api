package com.emsapi.integration.services;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.emsapi.services.JwtService;

@SpringBootTest
public class JwtServiceTests {
    @Autowired
    private JwtService jwtService;

    @Test
    public void retornaUserIdDoJWT() {
        UUID userId = UUID.randomUUID();

        String jwt = this.jwtService.generateJwt(userId.toString());

        assertThat(this.jwtService.validateJwt(jwt)).isEqualTo(userId.toString());
    }
}
