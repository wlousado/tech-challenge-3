package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentHistoryRepository;
import com.fiap.appointmentms.infra.stub.AppointmentHistoryStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentHistorySpringGatewayTest {

    @Mock
    private AppointmentHistoryRepository appointmentHistoryRepository;

    @InjectMocks
    private AppointmentHistorySpringGateway appointmentHistorySpringGateway;

    @Test
    @DisplayName("Should save appointment history successfully")
    void shouldSaveAppointmentHistorySuccessfully() {
        var toSaveEntity = AppointmentHistoryStub.craeteToSave();
        var toSaveAppoin = AppointmentHistoryStub.createToSaveDomain();

        when(appointmentHistoryRepository.save(any()))
                .thenReturn(toSaveEntity);

        var expect = appointmentHistorySpringGateway.save(toSaveAppoin);
        assertNotNull(expect);
        assertEquals(toSaveAppoin.id(), expect.id());
    }
}