package com.fiap.appointmentms.core.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.infra.presenter.UserPresenter;
import com.fiap.core.message.RegisterAppointmentMessage;

public class AppointmentMessagePresenter {

    private AppointmentMessagePresenter(){}

    public static RegisterAppointmentMessage toMessage(Appointment register) {
        return RegisterAppointmentMessage.builder()
                .doctor(UserPresenter.toMessage(register.doctor()))
                .registeredBy(UserPresenter.toMessage(register.registeredBy()))
                .patient(UserPresenter.toMessage(register.patient()))
                .dateTimeOfAppointment(register.dateTimeOfAppointment())
                .build();
    }
}
