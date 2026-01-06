package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.AppointmentEventEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Appointment(
        Long id,
        User patient,
        User doctor,
        User registeredBy,
        AppointmentEventEnum event,
        String observation,
        LocalDateTime dateTimeOfAppointment
        ) {
}
