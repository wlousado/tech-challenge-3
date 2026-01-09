package com.fiap.core.message;

public record AppointmentMessage(
        String descricao,
        UserMessage paciente,
        UserMessage medico,
        String data
) {
}
