package com.fiap.core.message;

import lombok.Builder;

@Builder
public record AppointmentCorrectionMessage(
        Long idAppointment,
        String correctedObservation,
        String justification
) {
}
