package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.AppointmentEventEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentView(
        Long id,
        Long idAppointment,
        AppointmentEventEnum event,
        LocalDateTime updatedAt
) {
}
