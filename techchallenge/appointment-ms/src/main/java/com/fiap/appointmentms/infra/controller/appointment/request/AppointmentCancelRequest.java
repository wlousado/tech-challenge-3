package com.fiap.appointmentms.infra.controller.appointment.request;

public record AppointmentCancelRequest(Long idAppointment,
                                       String cancellationReason) {
}
