package com.fiap.appointmentms.core.stub;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.User;
import com.fiap.core.enums.AppointmentEventEnum;

import java.time.LocalDateTime;

public class AppointmentStub {

    public static Appointment createStub(User doctor, User registeredBy, User patient, LocalDateTime localDateTime) {
        return Appointment.builder()
                .doctor(doctor)
                .registeredBy(registeredBy)
                .patient(patient)
                .dateTimeOfAppointment(localDateTime)
                .build();
    }

    public static Appointment createStub(Long id, User doctor, User registeredBy, User patient, LocalDateTime localDateTime) {
        return Appointment.builder()
                .id(id)
                .doctor(doctor)
                .registeredBy(registeredBy)
                .patient(patient)
                .dateTimeOfAppointment(localDateTime)
                .build();
    }

    public static Appointment createUpdateStub(long l) {
        return Appointment.builder()
                .id(l)
                .build();
    }

    public static Appointment createStub(AppointmentEventEnum event) {
        return Appointment.builder()
                .id(1L)
                .doctor(UserStub.createDoctor())
                .registeredBy(UserStub.createNurse())
                .patient(UserStub.createPatient())
                .dateTimeOfAppointment(LocalDateTime.now().plusDays(1))
                .event(event)
                .build();
    }
}
