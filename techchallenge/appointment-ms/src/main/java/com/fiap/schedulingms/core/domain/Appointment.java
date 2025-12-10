package com.fiap.schedulingms.core.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fiap.core.domain.User;


public record Appointment(
        Long id,
        User patient,
        User doctor,
        User nurse,
        LocalDate dateOfAppointment,
        LocalTime timeOfAppointment) {

    public static Appointment newAppointment(User patient, User doctor, User nurse, LocalDate dateOfAppointment, LocalTime timeOfAppointment){
        return new Appointment(null, patient, doctor, nurse, dateOfAppointment, timeOfAppointment);
    }
}
