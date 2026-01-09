package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentTimelineRepository;
import com.fiap.appointmentms.infra.stub.AppointmentTimelineStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppointmentTimelineSpringGatewayTest {

    @Mock
    private AppointmentTimelineRepository repository;

    @InjectMocks
    private AppointmentTimelineSpringGateway appointmentTimelineSpringGateway;


    @Test
    @DisplayName("should be save appointment timeline")
    void shouldBeSaveAppointmentTimeline() {
        var domain = AppointmentTimelineStub.createStub();
        var entity = AppointmentTimelineStub.createEntity();

        appointmentTimelineSpringGateway.save(domain);

        verify(repository, times(1)).save(any());
    }
}