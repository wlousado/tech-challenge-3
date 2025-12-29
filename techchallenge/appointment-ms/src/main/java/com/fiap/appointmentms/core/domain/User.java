package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.UserTypeEnum;

import java.time.LocalDateTime;

public record User(
        Long id,
        String name,
        String email,
        String login,
        String password,
        UserTypeEnum type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {


    public User(String username, String password, String authority) {
        this(null, username, username, username, password, UserTypeEnum.valueOf(authority), null, null);
    }

    public User(String joeDue, String mail, String medic, String crypt, UserTypeEnum userTypeEnum, LocalDateTime now, LocalDateTime now1) {
        this(null, joeDue, mail, medic, crypt, userTypeEnum, now, now1);
    }
}
