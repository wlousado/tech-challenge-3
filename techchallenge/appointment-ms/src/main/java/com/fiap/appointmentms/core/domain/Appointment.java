package com.fiap.appointmentms.core.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Appointment(
        Long id,
        User patient,
        User doctor,
        User registeredBy,
        String observation,
        LocalDateTime dateTimeOfAppointment
        ) {
}
