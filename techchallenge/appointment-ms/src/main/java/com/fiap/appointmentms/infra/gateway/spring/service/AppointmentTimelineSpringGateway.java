package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.core.domain.AppointmentTimeline;
import com.fiap.appointmentms.core.gateway.AppointmentTimelineGateway;
import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentTimelineRepository;
import com.fiap.appointmentms.infra.presenter.AppointmentTimelinePresenter;
import org.springframework.stereotype.Service;

@Service
public class AppointmentTimelineSpringGateway implements AppointmentTimelineGateway {

    private final AppointmentTimelineRepository repository;

    public AppointmentTimelineSpringGateway(AppointmentTimelineRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(AppointmentTimeline appointmentTimeline) {
        var entity = AppointmentTimelinePresenter.toEntity(appointmentTimeline);
        repository.save(entity);
    }
}
