package com.fiap.appointmentms.core.gateway;

import com.fiap.core.message.*;

public interface NotificationGateway {

    void registerAppointment(RegisterAppointmentMessage message);
    void updateAppointment(UpdateAppointmentMessage message);
    void correctionAppointment(CorrectionAppointmentMessage message);
    void cancelAppointment(CancelledAppointmentMessage message);
    void completeAppointment(CompletedAppointmentMessage message);
}
