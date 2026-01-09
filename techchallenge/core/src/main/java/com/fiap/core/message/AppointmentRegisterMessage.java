package com.fiap.core.message;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentRegisterMessage(
        Long idAppointment,
        UserMessage patient,
        UserMessage doctor,
        UserMessage registeredBy,
        LocalDateTime dateTimeOfAppointment
) {

}
