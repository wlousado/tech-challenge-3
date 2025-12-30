package com.fiap.core.message;

public record CorrectionAppointmentMessage(
        Long idAppointment,
        String correctedObservation,
        String justification
) {
}
