package com.fiap.appointmentms.core.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.infra.presenter.UserPresenter;
import com.fiap.core.message.AppointmentRegisterMessage;
import com.fiap.core.message.AppointmentUpdateMessage;

public class AppointmentMessagePresenter {

    private AppointmentMessagePresenter(){}

    public static AppointmentRegisterMessage toMessage(Appointment register) {
        return AppointmentRegisterMessage.builder()
                .idAppointment(register.id())
                .doctor(UserPresenter.toMessage(register.doctor()))
                .registeredBy(UserPresenter.toMessage(register.registeredBy()))
                .patient(UserPresenter.toMessage(register.patient()))
                .dateTimeOfAppointment(register.dateTimeOfAppointment())
                .build();
    }


    public static AppointmentUpdateMessage toMessage(AppointmentUpdate updateAppointment) {
        return AppointmentUpdateMessage.builder()
                .idAppointment(updateAppointment.idAppointment())
                .updatedObservation(updateAppointment.updatedObservation())
                .cid(updateAppointment.cid())
                .build();
    }
}
