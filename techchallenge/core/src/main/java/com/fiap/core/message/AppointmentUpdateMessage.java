package com.fiap.core.message;

import lombok.Builder;

@Builder
public record AppointmentUpdateMessage(
        Long idAppointment,
        String updatedObservation,
        String cid
) {
}
