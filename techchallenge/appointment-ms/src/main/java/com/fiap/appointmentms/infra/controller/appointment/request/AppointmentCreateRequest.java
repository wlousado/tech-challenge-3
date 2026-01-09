package com.fiap.appointmentms.infra.controller.appointment.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AppointmentCreateRequest(
        Long doctorId,
        Long registeredBy,
        Long patientId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime dateTimeOfAppointment
) {
}
