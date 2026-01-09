package com.fiap.appointmentms.infra.gateway.spring.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncryptSpringSecurityGatewayTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EncryptSpringSecurityGateway encryptSpringSecurityGateway;

    @Test
    @DisplayName("Should encrypt password successfully")
    void testEncrypt() {
        String rawPassword = "myPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(rawPassword))
                .thenReturn(encodedPassword);

        String result = encryptSpringSecurityGateway.encrypt(rawPassword);

        assertEquals(encodedPassword, result);
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    @DisplayName("Should validate password successfully")
    void testValidate() {
        String rawPassword = "myPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(true);

        Boolean result = encryptSpringSecurityGateway.validate(rawPassword, encodedPassword);

        assertTrue(result);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("Should invalidate password successfully")
    void testInvalidate() {
        String rawPassword = "myPassword";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(false);

        Boolean result = encryptSpringSecurityGateway.validate(rawPassword, encodedPassword);

        assertFalse(result);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }
}