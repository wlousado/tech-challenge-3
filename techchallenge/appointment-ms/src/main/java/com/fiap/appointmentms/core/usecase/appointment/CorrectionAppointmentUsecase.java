package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.domain.AppointmentCorrection;
import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.presenter.AppointmentMessagePresenter;
import com.fiap.appointmentms.core.presenter.AppointmentPresenter;
import com.fiap.core.enums.AppointmentEventEnum;
import org.springframework.stereotype.Service;

@Service
public class CorrectionAppointmentUsecase {

    private final AppointmentGateway appointmentGateway;
    private final AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    public CorrectionAppointmentUsecase(AppointmentGateway appointmentGateway, AppointmentEventSourcingGateway appointmentEventSourcingGateway) {
        this.appointmentGateway = appointmentGateway;
        this.appointmentEventSourcingGateway = appointmentEventSourcingGateway;
    }

    public void execute(AppointmentCorrection correction) {
        var appointment = appointmentGateway.findById(correction.idAppointment());
        if(appointment.isPresent()){
            var paramAppointment = appointment.get();
            if(!paramAppointment.event().equals(AppointmentEventEnum.COMPLETED_APPOINTMENT) && !paramAppointment.event().equals(AppointmentEventEnum.CANCELLED_APPOINTMENT)){
                var toCorrect = AppointmentPresenter.toDomain(paramAppointment, AppointmentEventEnum.CORRECTED_APPOINTMENT);
                appointmentGateway.update(toCorrect);
                var message = AppointmentMessagePresenter.toMessage(correction);
                appointmentEventSourcingGateway.correctionAppointment(message);
            } else throw new IllegalStateException("Appointment already finished");
        } else throw new IllegalStateException("Appointment not found");
    }
}
