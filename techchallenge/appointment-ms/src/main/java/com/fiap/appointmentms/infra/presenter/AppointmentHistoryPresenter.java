package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.AppointmentHistory;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentHistoryEntity;
import com.fiap.core.enums.AppointmentEventEnum;
import com.fiap.core.message.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppointmentHistoryPresenter {

    private AppointmentHistoryPresenter(){}

    public static AppointmentHistoryEntity toEntity(AppointmentHistory appointmentHistory) {
        return AppointmentHistoryEntity.builder()
                .id(Objects.isNull(appointmentHistory.id()) ? null : appointmentHistory.id())
                .idAppointment(appointmentHistory.idAppointment())
                .dateTimeOfChange(appointmentHistory.dateTimeOfChange())
                .event(appointmentHistory.event())
                .detail(appointmentHistory.detail())
                .build();
    }

    public static AppointmentHistory toDomain(AppointmentHistoryEntity result) {
        return AppointmentHistory.builder()
                .id(result.getId())
                .idAppointment(result.getIdAppointment())
                .dateTimeOfChange(result.getDateTimeOfChange())
                .event(result.getEvent())
                .detail(result.getDetail())
                .build();
    }

    public static AppointmentHistory toDomain(AppointmentRegisterMessage message, String serializedAppointment) {
        return AppointmentHistory.builder()
                .idAppointment(message.idAppointment())
                .event(AppointmentEventEnum.REGISTERED_APPOINTMENT)
                .dateTimeOfChange(message.dateTimeOfAppointment())
                .detail(serializedAppointment)
                .build();
    }

    public static AppointmentHistory toDomain(AppointmentUpdateMessage message, String serializedAppointment, LocalDateTime dateOfChange) {
        return AppointmentHistory.builder()
                .idAppointment(message.idAppointment())
                .event(AppointmentEventEnum.UPDATED_APPOINTMENT)
                .dateTimeOfChange(dateOfChange)
                .detail(serializedAppointment)
                .build();
    }

    public static AppointmentHistory toDomain(AppointmentCorrectionMessage message, String serializedAppointment, LocalDateTime dateOfChange) {
        return AppointmentHistory.builder()
                .idAppointment(message.idAppointment())
                .event(AppointmentEventEnum.CORRECTED_APPOINTMENT)
                .dateTimeOfChange(dateOfChange)
                .detail(serializedAppointment)
                .build();
    }

    public static AppointmentHistory toDomain(AppointmentCancelledMessage message, String serializedAppointment, LocalDateTime dateOfChange) {
        return AppointmentHistory.builder()
                .idAppointment(message.idAppointment())
                .event(AppointmentEventEnum.CANCELLED_APPOINTMENT)
                .dateTimeOfChange(dateOfChange)
                .detail(serializedAppointment)
                .build();
    }

    public static AppointmentHistory toDomain(AppointmentCompletedMessage message, String serializedAppointment, LocalDateTime dateOfChange) {
        return AppointmentHistory.builder()
                .idAppointment(message.idAppointment())
                .event(AppointmentEventEnum.COMPLETED_APPOINTMENT)
                .dateTimeOfChange(dateOfChange)
                .detail(serializedAppointment)
                .build();
    }
}
