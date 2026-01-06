package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.AppointmentTimeline;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentTimelineEntity;
import com.fiap.core.enums.AppointmentEventEnum;
import com.fiap.core.message.AppointmentRegisterMessage;
import com.fiap.core.message.AppointmentUpdateMessage;

import java.util.Objects;

public class AppointmentTimelinePresenter {

    private AppointmentTimelinePresenter(){}


    public static AppointmentTimeline toDomain(AppointmentRegisterMessage message) {
        return AppointmentTimeline.builder()
                .idAppointment(message.idAppointment())
                .doctor(message.doctor().id())
                .patient(message.patient().id())
                .registeredBy(message.registeredBy().id())
                .event(AppointmentEventEnum.REGISTERED_APPOINTMENT)
                .dateTimeOfAppointment(message.dateTimeOfAppointment())
                .build();
    }

    public static AppointmentTimelineEntity toEntity(AppointmentTimeline appointmentTimeline) {
        return AppointmentTimelineEntity.builder()
                .idAppointment(appointmentTimeline.idAppointment())
                .doctor(isNull(appointmentTimeline.doctor()))
                .patient(isNull(appointmentTimeline.patient()))
                .registeredBy(appointmentTimeline.registeredBy())
                .event(appointmentTimeline.event())
                .observation(isNullParam(appointmentTimeline.observation()))
                .cancellationReason(isNullParam(appointmentTimeline.cancellationReason()))
                .correctedObservation(isNullParam(appointmentTimeline.correctedObservation()))
                .justification(isNullParam(appointmentTimeline.justification()))
                .cid(isNullParam(appointmentTimeline.cid()))
                .dateTimeOfAppointment(Objects.isNull(appointmentTimeline.dateTimeOfAppointment()) ? null : appointmentTimeline.dateTimeOfAppointment())
                .updatedAt(appointmentTimeline.updatedAt())
                .build();
    }

    public static AppointmentTimeline toDomain(AppointmentUpdateMessage message) {
        return AppointmentTimeline.builder()
                .idAppointment(message.idAppointment())
                .updatedObservation(message.updatedObservation())
                .event(AppointmentEventEnum.UPDATED_APPOINTMENT)
                .build();
    }

    private static Long isNull(Long id) {
        return Objects.isNull(id) ? null : id;
    }

    private static String isNullParam(String param) {
        return Objects.isNull(param) ? null : param;
    }
}
