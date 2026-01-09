package com.fiap.appointmentms.core.gateway;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.AppointmentUpdate;

import java.util.Optional;

public interface AppointmentGateway {
    Appointment save(Appointment appointment);
    Optional<Appointment> findById(Long id);
    Appointment update(Appointment appointment);
}
