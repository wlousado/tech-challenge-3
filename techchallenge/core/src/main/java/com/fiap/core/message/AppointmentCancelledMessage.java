package com.fiap.core.message;

public record AppointmentCancelledMessage(
        Long idAppointment,
        String cancellationReason
) {
}
