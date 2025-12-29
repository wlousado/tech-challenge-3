package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.core.gateway.UserGateway;
import com.fiap.appointmentms.infra.gateway.spring.data.repository.UserRepository;
import com.fiap.appointmentms.infra.presenter.UserPresenter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSpringGateway implements UserGateway {

    private final UserRepository userRepository;

    public UserSpringGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        var user = userRepository.findByLogin(login);
        return user.map(UserPresenter::mapEntity);
    }

    @Override
    public void save(User doc) {
        var entity = UserPresenter.toEntity(doc);
        userRepository.save(entity);
    }
}
