package com.fiap.appointmentms.infra.gateway.spring.security;

import com.fiap.appointmentms.core.gateway.EncryptGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncryptSpringSecurityGateway implements EncryptGateway {

    private final PasswordEncoder passwordEncoder;

    public EncryptSpringSecurityGateway(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Boolean validate(String data, String encryptedData) {
        return passwordEncoder.matches(data, encryptedData);
    }
}
