package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.UserGateway;
import com.fiap.appointmentms.core.presenter.AppointmentMessagePresenter;
import com.fiap.appointmentms.core.presenter.AppointmentPresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BookAppointmentUsecase {

    private final AppointmentGateway appointmentGateway;
    private final UserGateway userGateway;
    private final AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    public BookAppointmentUsecase(AppointmentGateway appointmentGateway, UserGateway userGateway, AppointmentEventSourcingGateway appointmentEventSourcingGateway) {
        this.appointmentGateway = appointmentGateway;
        this.userGateway = userGateway;
        this.appointmentEventSourcingGateway = appointmentEventSourcingGateway;
    }

    public void execute(Appointment appointment) {
        User doctor = null;

        if(appointment.dateTimeOfAppointment().isBefore(LocalDateTime.now())){
            log.error("Data de agendamento deve ser maior que a data atual");
            throw new IllegalArgumentException("Data de agendamento deve ser maior que a data atual");
        }

        var registeredBy = userGateway.findByLogin(appointment.registeredBy().login())
                .orElseThrow();
        var patient = userGateway.findById(appointment.patient().id())
                .orElseThrow();

        if(registeredBy.id().equals(appointment.doctor().id())) {
            doctor = registeredBy;
        } else {
            doctor = userGateway.findById(appointment.doctor().id())
                    .orElseThrow();
        }

        var register = AppointmentPresenter.toDomain(appointment, doctor, registeredBy, patient);
        var saved = appointmentGateway.save(register);
        var appointmentRegistered = AppointmentMessagePresenter.toMessage(saved);
        appointmentEventSourcingGateway.registerAppointment(appointmentRegistered);
    }
}
