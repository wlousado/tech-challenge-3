package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentRepository;
import com.fiap.appointmentms.infra.presenter.AppointmentPresenter;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentPresenter::toDomain);
    }

    @Override
    public Appointment update(Appointment appointment) {
        var entity  = AppointmentPresenter.toEntity(appointment);
        return appointmentRepository.findOne(Example.of(entity))
                .map(f -> {
                    f.setObservation(appointment.observation());
                    f.setEvent(appointment.event());
            var result = appointmentRepository.save(f);
            return AppointmentPresenter.toDomain(result);
        }).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }
}
