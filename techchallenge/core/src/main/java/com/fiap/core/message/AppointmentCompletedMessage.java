package com.fiap.core.message;

import lombok.Builder;

@Builder
public record AppointmentCompletedMessage(Long idAppointment) {
}
