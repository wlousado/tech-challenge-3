package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.AppointmentEventEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentHistory (
        Long id,
        Long idAppointment,
        LocalDateTime dateTimeOfChange,
        AppointmentEventEnum event,
        String detail
) {
}
