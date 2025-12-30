package com.fiap.appointmentms.infra.presenter;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.infra.controller.appointment.request.AppointmentCreateRequest;
import com.fiap.appointmentms.infra.controller.appointment.response.AppointmentResponse;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentEntity;
import com.fiap.core.message.RegisterAppointmentMessage;

public class AppointmentPresenter {

    private AppointmentPresenter(){}

    public static Appointment toDomain(AppointmentCreateRequest request) {
        return Appointment.builder()
                .doctor(new User(request.doctorId()))
                .registeredBy(new User(request.registeredBy()))
                .patient(new User(request.patientId()))
                .dateTimeOfAppointment(request.dateTimeOfAppointment())
                .build();
    }

    public static Appointment toDomain(AppointmentEntity entity) {
        return new Appointment(
                entity.getId(),
                UserPresenter.mapEntity(entity.getDoctor()),
                UserPresenter.mapEntity(entity.getDoctor()),
                UserPresenter.mapEntity(entity.getDoctor()),
                entity.getObservation(),
                entity.getDateOfAppointment()
        );
    }

    public static AppointmentResponse toResponse(Long response) {
        return new AppointmentResponse(response);
    }

    public static AppointmentEntity toEntity(Appointment appointment) {
        return AppointmentEntity.builder()
                .doctor(UserPresenter.toEntity(appointment.doctor()))
                .registeredBy(UserPresenter.toEntity(appointment.registeredBy()))
                .patient(UserPresenter.toEntity(appointment.patient()))
                .observation(appointment.observation())
                .dateOfAppointment(appointment.dateTimeOfAppointment())
                .build();
    }

    public static Appointment toDomain(RegisterAppointmentMessage message) {
        return Appointment.builder()
                .doctor(UserPresenter.toDomain(message.doctor()))
                .registeredBy(UserPresenter.toDomain(message.registeredBy()))
                .patient(UserPresenter.toDomain(message.patient()))
                .dateTimeOfAppointment(message.dateTimeOfAppointment())
                .build();
    }
}
