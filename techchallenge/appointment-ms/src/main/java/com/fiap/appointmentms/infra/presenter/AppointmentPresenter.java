package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCreateRequest;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentEntity;
import com.fiap.core.enums.AppointmentEventEnum;
import com.fiap.core.message.AppointmentUpdateMessage;
import com.fiap.core.message.AppointmentRegisterMessage;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.Objects;

public class AppointmentPresenter {

    private AppointmentPresenter(){}

    public static Appointment toDomain(AppointmentCreateRequest request, Principal principal) {
        return Appointment.builder()
                .doctor(UserPresenter.toDomain(request.doctorId()))
                .registeredBy(UserPresenter.toDomain(principal.getName()))
                .patient(UserPresenter.toDomain(request.patientId()))
                .dateTimeOfAppointment(request.dateTimeOfAppointment())
                .event(AppointmentEventEnum.REGISTERED_APPOINTMENT)
                .build();
    }

    public static Appointment toDomain(AppointmentEntity entity) {
        return Appointment.builder()
                .id(entity.getId())
                .doctor(UserPresenter.mapEntity(entity.getDoctor()))
                .registeredBy(UserPresenter.mapEntity(entity.getRegisteredBy()))
                .patient(UserPresenter.mapEntity(entity.getPatient()))
                .observation(entity.getObservation())
                .event(entity.getEvent())
                .dateTimeOfAppointment(entity.getDateOfAppointment())
                .build(
        );
    }

    public static AppointmentEntity toEntity(Appointment appointment) {
        return AppointmentEntity.builder()
                .id(Objects.isNull(appointment.id()) ? null : appointment.id())
                .doctor(UserPresenter.toEntity(appointment.doctor()))
                .registeredBy(UserPresenter.toEntity(appointment.registeredBy()))
                .patient(UserPresenter.toEntity(appointment.patient()))
                .observation(appointment.observation())
                .dateOfAppointment(appointment.dateTimeOfAppointment())
                .event(appointment.event())
                .build();
    }

    public static Appointment toDomain(AppointmentRegisterMessage message) {
        return Appointment.builder()
                .doctor(UserPresenter.toDomain(message.doctor()))
                .registeredBy(UserPresenter.toDomain(message.registeredBy()))
                .patient(UserPresenter.toDomain(message.patient()))
                .dateTimeOfAppointment(message.dateTimeOfAppointment())
                .build();
    }

    public static Appointment toDomain(AppointmentUpdateMessage message) {
        return Appointment.builder()
                .id(message.idAppointment())
                .observation(message.observations())
                .build();
    }

    public static Appointment mapUpdateMessageToDomain(AppointmentUpdateMessage message) {
        return null;
    }
}
