package com.fiap.core.message;

public record UpdateAppointmentMessage(
        Long idAppointment,
        String observations,
        String cid
) {
}
