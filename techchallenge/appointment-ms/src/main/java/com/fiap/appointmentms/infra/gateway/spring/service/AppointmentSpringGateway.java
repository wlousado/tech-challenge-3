package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentEntity;
import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentRepository;
import com.fiap.appointmentms.infra.presenter.AppointmentPresenter;
import org.springframework.stereotype.Service;

@Service
public class AppointmentSpringGateway implements AppointmentGateway {

    private final AppointmentRepository appointmentRepository;

    public AppointmentSpringGateway(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        var entity = AppointmentPresenter.toEntity(appointment);
         var result = appointmentRepository.save(entity);
        return AppointmentPresenter.toDomain(result);
    }
}
