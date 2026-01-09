package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentUpdateRequest;
import com.fiap.core.enums.AppointmentEventEnum;
import com.fiap.core.message.AppointmentUpdateMessage;

public class AppointmentUpdatePresenter {

    private AppointmentUpdatePresenter(){}


    public static AppointmentUpdate toUpdateDomain(AppointmentUpdateRequest request) {
        return AppointmentUpdate.builder()
                .idAppointment(request.idAppointment())
                .observations(request.observations())
                .cid(request.cid())
                .event(AppointmentEventEnum.UPDATED_APPOINTMENT)
                .build();
    }

    public static AppointmentUpdateMessage toMessage(Appointment appointment, AppointmentUpdate update) {
        return AppointmentUpdateMessage.builder()
                .idAppointment(appointment.id())
                .observations(appointment.observation())
                .cid(update.cid())
                .build();
    }
}
