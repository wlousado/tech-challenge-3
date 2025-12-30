package com.fiap.core.message;

import com.fiap.core.enums.UserTypeEnum;

public record UserMessage(
        Long id,
        String name,
        String email,
        UserTypeEnum type
) {
}
