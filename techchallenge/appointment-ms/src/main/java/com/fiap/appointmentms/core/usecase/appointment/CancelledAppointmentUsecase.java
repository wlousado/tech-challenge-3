package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.domain.AppointmentCancelled;
import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.presenter.AppointmentMessagePresenter;
import com.fiap.appointmentms.core.presenter.AppointmentPresenter;
import com.fiap.core.enums.AppointmentEventEnum;
import org.springframework.stereotype.Service;

@Service
public class CancelledAppointmentUsecase {

    private final AppointmentGateway appointmentGateway;
    private final AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    public CancelledAppointmentUsecase(AppointmentGateway appointmentGateway, AppointmentEventSourcingGateway appointmentEventSourcingGateway) {
        this.appointmentGateway = appointmentGateway;
        this.appointmentEventSourcingGateway = appointmentEventSourcingGateway;
    }

    public void execute(AppointmentCancelled appointmentCancelled){
        var appointment = appointmentGateway.findById(appointmentCancelled.idAppointment());
        if(appointment.isPresent()){
            var toCancell = AppointmentPresenter.toDomain(appointment.get(), AppointmentEventEnum.CANCELLED_APPOINTMENT);
            appointmentGateway.update(toCancell);
            var message = AppointmentMessagePresenter.toMessage(appointmentCancelled);
            appointmentEventSourcingGateway.cancelAppointment(message);
        } else throw new IllegalStateException("Appointment not found");
    }
}
