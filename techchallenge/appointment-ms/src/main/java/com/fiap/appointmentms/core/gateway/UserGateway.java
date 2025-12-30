package com.fiap.appointmentms.core.gateway;

import com.fiap.appointmentms.core.domain.User;

import java.util.Optional;

public interface  UserGateway {

    Optional<User> findByLogin(String login);

    void save(User doc);

    Optional<User> findById(Long id);
}
