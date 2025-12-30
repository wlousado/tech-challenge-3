package com.fiap.appointmentms.core.gateway;

import com.fiap.appointmentms.core.domain.Appointment;

public interface AppointmentGateway {
    Appointment save(Appointment appointment);
}
