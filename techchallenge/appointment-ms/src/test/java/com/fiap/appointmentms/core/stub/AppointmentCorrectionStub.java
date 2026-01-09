package com.fiap.appointmentms.core.stub;

import com.fiap.appointmentms.core.domain.AppointmentCorrection;

public class AppointmentCorrectionStub {
    public static AppointmentCorrection createStub() {
        return new AppointmentCorrection(
                1L,
                "Corrected details for the appointment",
                "SystemUser"
        );
    }
}
