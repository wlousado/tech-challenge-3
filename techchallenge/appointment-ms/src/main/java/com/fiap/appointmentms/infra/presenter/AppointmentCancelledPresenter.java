package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.AppointmentCancelled;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCancelRequest;

public class AppointmentCancelledPresenter {

    public static AppointmentCancelled toDomain(AppointmentCancelRequest request) {
        return AppointmentCancelled.builder()
                .idAppointment(request.idAppointment())
                .cancellationReason(request.cancellationReason())
                .build();
    }
}
