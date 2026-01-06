package com.fiap.appointmentms.core.gateway;

import com.fiap.appointmentms.core.domain.AppointmentView;

public interface AppointmentViewGateway {

    void save(AppointmentView appointmentView);
    void update(AppointmentView appointmentView);
}
