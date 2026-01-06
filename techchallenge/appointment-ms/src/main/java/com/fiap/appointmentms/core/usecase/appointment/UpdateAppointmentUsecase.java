package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.domain.AppointmentUpdate;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.presenter.AppointmentMessagePresenter;
import com.fiap.appointmentms.core.presenter.AppointmentPresenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpdateAppointmentUsecase {

    private final AppointmentGateway appointmentGateway;

    private final AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    public UpdateAppointmentUsecase(AppointmentGateway appointmentGateway, AppointmentEventSourcingGateway appointmentEventSourcingGateway) {
        this.appointmentGateway = appointmentGateway;
        this.appointmentEventSourcingGateway = appointmentEventSourcingGateway;
    }

    public void execute(AppointmentUpdate updateAppointment){
        var appointment = appointmentGateway.findById(updateAppointment.idAppointment());
        if(appointment.isPresent()){
            var toUpdate = AppointmentPresenter.toDomain(appointment.get(), updateAppointment);
            appointmentGateway.update(toUpdate);
            var message = AppointmentMessagePresenter.toMessage(updateAppointment);
            appointmentEventSourcingGateway.updateAppointment(message);
        } else throw new IllegalStateException("Appointment not found");
    }
}
