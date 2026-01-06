package com.fiap.appointmentms.core.gateway;

import com.fiap.core.message.*;

public interface AppointmentEventSourcingGateway {

    void registerAppointment(AppointmentRegisterMessage message);
    void updateAppointment(AppointmentUpdateMessage message);
    void correctionAppointment(AppointmentCorrectionMessage message);
    void cancelAppointment(AppointmentCancelledMessage message);
    void completeAppointment(AppointmentCompletedMessage message);
}
