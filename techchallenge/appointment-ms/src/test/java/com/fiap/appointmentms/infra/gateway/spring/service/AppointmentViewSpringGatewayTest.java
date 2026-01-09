package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentViewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AppointmentViewSpringGatewayTest {

    @Mock
    private AppointmentViewRepository appointmentViewRepository;

    @InjectMocks
    private AppointmentViewSpringGateway appointmentViewSpringGateway;

    @Test
    @DisplayName("Should create save AppointmentViewSpringGateway")
    void shouldCreateSaveAppointmentViewSpringGateway() {

    }
}