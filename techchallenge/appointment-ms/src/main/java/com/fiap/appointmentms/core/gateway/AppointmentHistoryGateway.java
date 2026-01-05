package com.fiap.appointmentms.core.gateway;

import com.fiap.appointmentms.core.domain.AppointmentHistory;

public interface AppointmentHistoryGateway {

    AppointmentHistory save(AppointmentHistory appointmentHistory);
}
