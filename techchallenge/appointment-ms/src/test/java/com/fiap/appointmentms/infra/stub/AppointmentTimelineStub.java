package com.fiap.appointmentms.infra.stub;

import com.fiap.appointmentms.core.domain.AppointmentTimeline;
import com.fiap.appointmentms.infra.gateway.spring.data.entity.AppointmentTimelineEntity;

public class AppointmentTimelineStub {

    public static AppointmentTimeline createStub() {
        return AppointmentTimeline.builder()
                .build();
    }

    public static AppointmentTimelineEntity createEntity() {
        return AppointmentTimelineEntity.builder()

                .build();
    }
}
