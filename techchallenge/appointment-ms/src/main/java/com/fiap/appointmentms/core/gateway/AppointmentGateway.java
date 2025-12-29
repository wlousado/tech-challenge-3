package com.fiap.appointmentms.core.gateway;

import com.fiap.appointmentms.core.domain.Appointment;

public interface AppointmentGateway {
    void save(Appointment appointment);
}
