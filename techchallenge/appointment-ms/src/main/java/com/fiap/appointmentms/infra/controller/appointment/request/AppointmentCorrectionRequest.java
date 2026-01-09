package com.fiap.appointmentms.infra.controller.appointment.request;

public record AppointmentCorrectionRequest(
        Long idAppointment,
        String correctedObservation,
        String justification
) {
}
