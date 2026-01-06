package com.fiap.appointmentms.core.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentCancelled;
import com.fiap.appointmentms.core.domain.AppointmentCorrection;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.infra.presenter.UserPresenter;
import com.fiap.core.message.*;

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

    public static AppointmentCorrectionMessage toMessage(AppointmentCorrection correction) {
        return AppointmentCorrectionMessage.builder()
                .idAppointment(correction.idAppointment())
                .correctedObservation(correction.correctedObservation())
                .justification(correction.justification())
                .build();
    }

    public static AppointmentCancelledMessage toMessage(AppointmentCancelled appointmentCancelled) {
        return AppointmentCancelledMessage.builder()
                .idAppointment(appointmentCancelled.idAppointment())
                .cancellationReason(appointmentCancelled.cancellationReason())
                .build();
    }

    public static AppointmentCompletedMessage toMessage(Long idAppointment) {
        return AppointmentCompletedMessage.builder()
                .idAppointment(idAppointment)
                .build();
    }
}
