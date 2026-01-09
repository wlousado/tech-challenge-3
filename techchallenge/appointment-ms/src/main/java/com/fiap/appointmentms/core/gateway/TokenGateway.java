package com.fiap.appointmentms.core.gateway;

import com.fiap.appointmentms.core.domain.User;

public interface TokenGateway {
    String generate(User user);
    boolean validate(String token, User user);
    String extract(String token);
}
