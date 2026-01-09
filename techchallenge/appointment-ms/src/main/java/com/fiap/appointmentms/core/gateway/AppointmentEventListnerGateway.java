package com.fiap.appointmentms.core.gateway;

import com.fiap.core.message.*;

public interface AppointmentEventListnerGateway {

    void doOnRegisterAppointment(AppointmentRegisterMessage message);
    void doOnUpdateAppointment(AppointmentUpdateMessage message);
    void doOnCorrectionAppointment(AppointmentCorrectionMessage message);
    void doOnCancelAppointment(AppointmentCancelledMessage message);
    void doOnCompleteAppointment(AppointmentCompletedMessage message);
}
