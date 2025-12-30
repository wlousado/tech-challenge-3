package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.UserTypeEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record User(
        Long id,
        String name,
        String email,
        String login,
        String password,
        UserTypeEnum type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {


    public User(Long id) {
        this(id, null, null, null, null, null, null, null);
    }

    public User(String username, String password, String authority) {
        this(null, username, username, username, password, UserTypeEnum.valueOf(authority), null, null);
    }

    public User(String joeDue, String mail, String medic, String crypt, UserTypeEnum userTypeEnum, LocalDateTime now, LocalDateTime now1) {
        this(null, joeDue, mail, medic, crypt, userTypeEnum, now, now1);
    }

    public User(Long id, String name, String email, UserTypeEnum type) {
        this(null, name, email, email, null, type, null, null);
    }
}
