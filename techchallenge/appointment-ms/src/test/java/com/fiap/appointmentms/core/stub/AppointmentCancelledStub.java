package com.fiap.appointmentms.core.stub;

import com.fiap.appointmentms.core.domain.AppointmentCancelled;
import scala.App;

public class AppointmentCancelledStub {
    public static AppointmentCancelled createCancelledStub(Long id) {
        return new AppointmentCancelled(
                id,
                "Appointment cancelled successfully"
        );
    }
}
