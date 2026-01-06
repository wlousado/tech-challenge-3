package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.core.domain.AppointmentView;
import com.fiap.appointmentms.core.gateway.AppointmentViewGateway;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentViewEntity;
import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentViewRepository;
import com.fiap.appointmentms.infra.presenter.AppointmentViewPresenter;
import org.springframework.stereotype.Service;

@Service
public class AppointmentViewSpringGateway implements AppointmentViewGateway {

    private final AppointmentViewRepository appointmentViewRepository;

    public AppointmentViewSpringGateway(AppointmentViewRepository appointmentViewRepository) {
        this.appointmentViewRepository = appointmentViewRepository;
    }

    @Override
    public void save(AppointmentView appointmentView) {
        var entity = AppointmentViewPresenter.toEntity(appointmentView);
        appointmentViewRepository.save(entity);
    }
}
