package com.fiap.appointmentms.core.domain;

import com.fiap.core.enums.AppointmentEventEnum;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentTimeline(
        Long id,
        Long idAppointment,
        Long patient,
        Long doctor,
        Long registeredBy,
        String observation,
        String cancellationReason,
        String correctedObservation,
        String justification,
        String observations,
        String cid,
        LocalDateTime dateTimeOfAppointment,
        AppointmentEventEnum event,
        LocalDateTime updatedAt
) {
}
