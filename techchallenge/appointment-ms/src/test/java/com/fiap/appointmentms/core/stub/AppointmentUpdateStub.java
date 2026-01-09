package com.fiap.appointmentms.core.stub;

import com.fiap.appointmentms.core.domain.AppointmentUpdate;

public class AppointmentUpdateStub {

    public static AppointmentUpdate createUpdateStub(long l) {
        return AppointmentUpdate.builder()
                .idAppointment(l)
                .build();
    }

    public static AppointmentUpdate createUpdateStubExpected(long l) {
        return AppointmentUpdate.builder()
                .idAppointment(l)
                .build();
    }
}
