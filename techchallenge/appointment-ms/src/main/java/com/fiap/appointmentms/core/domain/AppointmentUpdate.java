package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.AppointmentEventEnum;
import lombok.Builder;

@Builder
public record AppointmentUpdate(
        Long idAppointment,
        String observations,
        String cid,
        AppointmentEventEnum event
) {
}
