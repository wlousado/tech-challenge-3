package com.fiap.appointmentms.core.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.User;

public class AppointmentPresenter {

    private AppointmentPresenter(){}


    public static Appointment toDomain(Appointment appointment, User doctor, User registeredBy, User patient) {
        return Appointment.builder()
                .doctor(doctor)
                .registeredBy(registeredBy)
                .patient(patient)
                .observation(appointment.observation())
                .dateTimeOfAppointment(appointment.dateTimeOfAppointment())
                .build();
    }
}
