package com.fiap.appointmentms.infra.gateway.spring.security;

import com.fiap.appointmentms.core.stub.UserStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TokenSpringSecurityGatewayTest {


    @InjectMocks
    private TokenSpringSecurityGateway tokenSpringSecurityGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(tokenSpringSecurityGateway, "jwtKey", "Zm9vYmFyMTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6MTIzNA==");
        ReflectionTestUtils.setField(tokenSpringSecurityGateway, "jwtExpiration", 3600L);
    }

    @Test
    @DisplayName("Should generate a token successfully")
    void shouldGenerateTokenSuccessfully() {

        var user = UserStub.createDoctor();

        var token = tokenSpringSecurityGateway.generate(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should validate a valid token successfully")
    void shouldValidateValidTokenSuccessfully() {
        var user = UserStub.createDoctor();
        var token = tokenSpringSecurityGateway.generate(user);

        var isValid = tokenSpringSecurityGateway.validate(token, user);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should not validate an invalid token")
    void shouldNotValidateInvalidToken() {
        var user = UserStub.createDoctor();
        var invalidToken = "invalid.token.here";
        var isValid = tokenSpringSecurityGateway.validate(invalidToken, user);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract subject from token successfully")
    void shouldExtractSubjectFromTokenSuccessfully() {
        var user = UserStub.createDoctor();
        var token = tokenSpringSecurityGateway.generate(user);

        var subject = tokenSpringSecurityGateway.extract(token);
        assertEquals(user.login(), subject);
    }
}