package com.fiap.appointmentms.core.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentCorrection;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.core.domain.User;
import com.fiap.core.enums.AppointmentEventEnum;

public class AppointmentPresenter {

    private AppointmentPresenter(){}


    public static Appointment toDomain(Appointment appointment, User doctor, User registeredBy, User patient) {
        return Appointment.builder()
                .doctor(doctor)
                .registeredBy(registeredBy)
                .patient(patient)
                .observation(appointment.observation())
                .dateTimeOfAppointment(appointment.dateTimeOfAppointment())
                .event(AppointmentEventEnum.REGISTERED_APPOINTMENT)
                .build();
    }

    public static Appointment toDomain(Appointment appointment, AppointmentUpdate updateAppointment) {
        return Appointment.builder()
                .id(appointment.id())
                .doctor(appointment.doctor())
                .registeredBy(appointment.registeredBy())
                .patient(appointment.patient())
                .observation(updateAppointment.updatedObservation())
                .dateTimeOfAppointment(appointment.dateTimeOfAppointment())
                .event(updateAppointment.event())
                .build();
    }

    public static Appointment toDomain(Appointment appointment, AppointmentEventEnum event) {
        return Appointment.builder()
                .id(appointment.id())
                .doctor(appointment.doctor())
                .registeredBy(appointment.registeredBy())
                .patient(appointment.patient())
                .observation(appointment.observation())
                .dateTimeOfAppointment(appointment.dateTimeOfAppointment())
                .event(event)
                .build();
    }
}
