package com.fiap.core.domain;

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


}
