package com.fiap.appointmentms.core.domain;

import lombok.Builder;

@Builder
public record AppointmentCorrection(
        Long idAppointment,
        String correctedObservation,
        String justification
) {
}
