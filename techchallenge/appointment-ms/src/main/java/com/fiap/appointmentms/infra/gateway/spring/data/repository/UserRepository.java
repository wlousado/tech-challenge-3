package com.fiap.appointmentms.infra.gateway.spring.data.repository;

import com.fiap.appointmentms.infra.gateway.spring.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String username);

    Optional<UserEntity> findByLoginOrEmail(String login, String email);
}
