package com.fiap.core.message;

import lombok.Builder;

@Builder
public record AppointmentCancelledMessage(
        Long idAppointment,
        String cancellationReason
) {
}
