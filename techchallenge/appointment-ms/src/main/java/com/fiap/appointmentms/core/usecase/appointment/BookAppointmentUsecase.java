package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.gateway.UserGateway;
import com.fiap.appointmentms.core.domain.Appointment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookAppointmentUsecase {

    private final AppointmentGateway appointmentGateway;

    public BookAppointmentUsecase(AppointmentGateway appointmentGateway, UserGateway userGateway) {
        this.appointmentGateway = appointmentGateway;
    }

    public Long execute(Appointment appointment) {
        appointmentGateway.save(appointment);
        return null;
    }
}
