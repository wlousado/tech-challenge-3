package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.core.domain.AppointmentHistory;
import com.fiap.appointmentms.core.gateway.AppointmentHistoryGateway;
import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentHistoryRepository;
import com.fiap.appointmentms.infra.presenter.AppointmentHistoryPresenter;
import org.springframework.stereotype.Service;

@Service
public class AppointmentHistorySpringGateway implements AppointmentHistoryGateway {

    private final AppointmentHistoryRepository appointmentHistoryRepository;

    public AppointmentHistorySpringGateway(AppointmentHistoryRepository appointmentHistoryRepository) {
        this.appointmentHistoryRepository = appointmentHistoryRepository;
    }

    @Override
    public AppointmentHistory save(AppointmentHistory appointmentHistory) {
        var result = appointmentHistoryRepository.save(AppointmentHistoryPresenter.toEntity(appointmentHistory));
        return AppointmentHistoryPresenter.toDomain(result);
    }
}
