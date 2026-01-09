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

    public AppointmentRegisterMessage addIdAppointment(Long id){
        return new AppointmentRegisterMessage(id, this.patient, this.doctor, this.registeredBy, this.dateTimeOfAppointment);
    }
}
