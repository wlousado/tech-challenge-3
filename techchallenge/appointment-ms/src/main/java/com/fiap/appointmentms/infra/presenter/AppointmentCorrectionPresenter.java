package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.AppointmentCorrection;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCorrectionRequest;
import jakarta.validation.Valid;

public class AppointmentCorrectionPresenter {

    private AppointmentCorrectionPresenter(){}


    public static AppointmentCorrection toDomain(AppointmentCorrectionRequest request) {
        return AppointmentCorrection.builder()
                .idAppointment(request.idAppointment())
                .justification(request.justification())
                .correctedObservation(request.correctedObservation())
                .build();
    }
}
