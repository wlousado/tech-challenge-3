package com.fiap.core.enums;

import lombok.Getter;

@Getter
public enum AppointmentEventEnum {
    REGISTERED_APPOINTMENT,
    CORRECTED_APPOINTMENT,
    UPDATED_APPOINTMENT,
    CANCELLED_APPOINTMENT,
    COMPLETED_APPOINTMENT
}
