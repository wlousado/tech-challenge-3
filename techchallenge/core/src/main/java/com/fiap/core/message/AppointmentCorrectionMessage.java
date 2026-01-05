package com.fiap.core.message;

public record AppointmentCorrectionMessage(
        Long idAppointment,
        String correctedObservation,
        String justification
) {
}
