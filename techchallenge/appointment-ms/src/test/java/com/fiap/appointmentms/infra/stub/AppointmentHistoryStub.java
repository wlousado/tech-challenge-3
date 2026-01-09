package com.fiap.appointmentms.infra.stub;

import com.fiap.appointmentms.core.domain.AppointmentHistory;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentHistoryEntity;
import com.fiap.core.enums.AppointmentEventEnum;

import java.time.LocalDateTime;

public class AppointmentHistoryStub {
    public static AppointmentHistoryEntity craeteToSave() {
        return AppointmentHistoryEntity.builder()
                .idAppointment(1L)
                .event(AppointmentEventEnum.REGISTERED_APPOINTMENT)
                .dateTimeOfChange(LocalDateTime.now().plusDays(1))
                .detail("Test detail")
                .build();
    }

    public static AppointmentHistory createToSaveDomain() {
        return AppointmentHistory.builder()
                .idAppointment(1L)
                .event(AppointmentEventEnum.REGISTERED_APPOINTMENT)
                .dateTimeOfChange(LocalDateTime.now().plusDays(1))
                .detail("Test detail")
                .build();
    }
}
