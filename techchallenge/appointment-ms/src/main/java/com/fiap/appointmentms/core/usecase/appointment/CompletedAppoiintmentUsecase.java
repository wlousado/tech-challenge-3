package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.presenter.AppointmentMessagePresenter;
import com.fiap.appointmentms.core.presenter.AppointmentPresenter;
import com.fiap.core.enums.AppointmentEventEnum;
import org.springframework.stereotype.Service;

@Service
public class CompletedAppoiintmentUsecase {

    private final AppointmentGateway appointmentGateway;
    private final AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    public CompletedAppoiintmentUsecase(AppointmentGateway appointmentGateway, AppointmentEventSourcingGateway appointmentEventSourcingGateway) {
        this.appointmentGateway = appointmentGateway;
        this.appointmentEventSourcingGateway = appointmentEventSourcingGateway;
    }

    public void execute(Long idAppointment){
        var appointment = appointmentGateway.findById(idAppointment);
        if(appointment.isPresent()){
            var toComplete = AppointmentPresenter.toDomain(appointment.get(), AppointmentEventEnum.COMPLETED_APPOINTMENT);
            appointmentGateway.update(toComplete);
            var message = AppointmentMessagePresenter.toMessage(idAppointment);
            appointmentEventSourcingGateway.completeAppointment(message);
        } else throw new IllegalStateException("Appointment not found");
    }
}
