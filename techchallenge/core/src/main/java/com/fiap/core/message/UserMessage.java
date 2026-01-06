package com.fiap.core.message;

import com.fiap.core.enums.UserTypeEnum;
import lombok.Builder;

@Builder
public record UserMessage(
        Long id,
        String name,
        String email,
        UserTypeEnum type
) {
}
