package com.fiap.appointmentms.core.domain;

import lombok.Builder;

@Builder
public record AppointmentCancelled(
        Long idAppointment,
        String cancellationReason
) {
}
