package com.fiap.appointmentms.infra.controller.auth.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
