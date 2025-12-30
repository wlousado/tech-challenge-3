package com.fiap.core.message;

public record CancelledAppointmentMessage(
        Long idAppointment,
        String cancellationReason
) {
}
