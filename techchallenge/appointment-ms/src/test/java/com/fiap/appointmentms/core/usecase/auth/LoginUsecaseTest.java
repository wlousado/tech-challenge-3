package com.fiap.appointmentms.core.usecase.auth;

import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.core.gateway.EncryptGateway;
import com.fiap.appointmentms.core.gateway.TokenGateway;
import com.fiap.appointmentms.core.gateway.UserGateway;
import com.fiap.appointmentms.core.stub.UserStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUsecaseTest {

    @Mock
    private EncryptGateway encryptGateway;

    @Mock
    private TokenGateway tokenGateway;

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private LoginUsecase loginUsecase;

    @Test
    @DisplayName("should create token when login and password are correct")
    void testExecute_Success() {
        String expected = "validToken";
        var user = UserStub.createDoctor();

        when(userGateway.findByLogin(user.login()))
            .thenReturn(Optional.of(user));

        when(encryptGateway.validate(anyString(), anyString()))
            .thenReturn(Boolean.TRUE);

        when(tokenGateway.generate(user))
            .thenReturn(expected);

        var token = loginUsecase.execute("doctor", "pass123");

        assertNotNull(token);
        assertEquals(expected, token);
    }

    @Test
    @DisplayName("should throw exception when user not found")
    void testExecute_InvalidLogin() {
        when(userGateway.findByLogin("invalidUser"))
            .thenReturn(Optional.empty());

        var exception = assertThrows(RuntimeException.class, () -> {
            loginUsecase.execute("invalidUser", "anyPassword");
        });

        assertEquals("Login invalidUser falhou", exception.getMessage());
    }

    @Test
    @DisplayName("should throw exception when password is incorrect")
    void testExecute_InvalidPassword() {
        var user = UserStub.createDoctor();

        when(userGateway.findByLogin(user.login()))
            .thenReturn(Optional.of(user));

        when(encryptGateway.validate(anyString(), anyString()))
            .thenReturn(Boolean.FALSE);

        var exception = assertThrows(RuntimeException.class, () -> {
            loginUsecase.execute("doctor", "wrongPassword");
        });

        assertEquals("Senha invalida", exception.getMessage());
    }
}