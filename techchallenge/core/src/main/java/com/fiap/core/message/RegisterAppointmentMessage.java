package com.fiap.core.message;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RegisterAppointmentMessage(
        Long idAppointment,
        UserMessage patient,
        UserMessage doctor,
        UserMessage registeredBy,
        LocalDateTime dateTimeOfAppointment
) {

    public RegisterAppointmentMessage addIdAppointment(Long id){
        return new RegisterAppointmentMessage(id, this.patient, this.doctor, this.registeredBy, this.dateTimeOfAppointment);
    }
}
