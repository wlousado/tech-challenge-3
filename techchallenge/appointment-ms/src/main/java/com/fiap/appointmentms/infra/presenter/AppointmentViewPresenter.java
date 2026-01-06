package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.AppointmentView;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentViewEntity;
import com.fiap.core.enums.AppointmentEventEnum;
import com.fiap.core.message.AppointmentRegisterMessage;
import com.fiap.core.message.AppointmentUpdateMessage;

import java.time.LocalDateTime;

public class AppointmentViewPresenter {

    private AppointmentViewPresenter(){}

    public static AppointmentView toDomain(AppointmentRegisterMessage message, LocalDateTime now) {
        return AppointmentView.builder()
                .idAppointment(message.idAppointment())
                .event(AppointmentEventEnum.REGISTERED_APPOINTMENT)
                .updatedAt(now)
                .build();
    }

    public static AppointmentViewEntity toEntity(AppointmentView appointmentView) {
        return AppointmentViewEntity.builder()
                .idAppointment(appointmentView.idAppointment())
                .event(appointmentView.event())
                .updatedAt(appointmentView.updatedAt())
                .build();
    }

    public static AppointmentView toDomain(AppointmentUpdateMessage message, LocalDateTime now) {
        return AppointmentView.builder()
                .idAppointment(message.idAppointment())
                .event(AppointmentEventEnum.UPDATED_APPOINTMENT)
                .updatedAt(now)
                .build();
    }
}
