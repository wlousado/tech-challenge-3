package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.AppointmentEventEnum;

import java.time.LocalDateTime;

public record AppointmentHistory (
        Long id,
        Long idAppointment,
        LocalDateTime dateTimeOfChange,
        AppointmentEventEnum event,
        String detail
) {
}
