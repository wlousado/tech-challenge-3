package com.fiap.core.message;

import lombok.Builder;

@Builder
public record AppointmentUpdateMessage(
        Long idAppointment,
        String observations,
        String cid
) {
}
