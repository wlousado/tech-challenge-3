package com.fiap.appointmentms.infra.controller.appointment.request;

public record AppointmentUpdateRequest(
        Long idAppointment,
        String observations,
        String cid
) {
}
