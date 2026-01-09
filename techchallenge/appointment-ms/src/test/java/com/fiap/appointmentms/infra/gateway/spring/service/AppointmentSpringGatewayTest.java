package com.fiap.appointmentms.infra.gateway.spring.service;

import com.fiap.appointmentms.core.stub.AppointmentStub;
import com.fiap.appointmentms.infra.gateway.spring.data.repository.AppointmentRepository;
import com.fiap.core.enums.AppointmentEventEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentSpringGatewayTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentSpringGateway appointmentSpringGateway;

    @Test
    @DisplayName("should save appointment")
    void shouldSaveAppointment() {
        var toSaveDomain = AppointmentStub.createStub(AppointmentEventEnum.REGISTERED_APPOINTMENT);
        var toSaveEntity = AppointmentStub.createEntity();

        when(appointmentRepository.save(any()))
                .thenReturn(toSaveEntity);

        var savedAppointment = appointmentSpringGateway.save(toSaveDomain);
        assertNotNull(savedAppointment);
        assertEquals(toSaveDomain.doctor().id(), savedAppointment.doctor().id());
    }

    @Test
    @DisplayName("should find appointment by id")
    void shouldFindAppointmentById() {
        var toFindEntity = AppointmentStub.createEntity();
        var expectedDomain = AppointmentStub.createStub(AppointmentEventEnum.REGISTERED_APPOINTMENT);

        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.of(toFindEntity));

        var foundAppointment = appointmentSpringGateway.findById(1L);
        assertTrue(foundAppointment.isPresent());
        assertEquals(expectedDomain.id(), foundAppointment.get().id());
    }

    @Test
    @DisplayName("should not found appointment by id")
    void shouldNotFoundAppointmentById() {
        when(appointmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        var foundAppointment = appointmentSpringGateway.findById(1L);
        assertFalse(foundAppointment.isPresent());
    }

    @Test
    @DisplayName("should update appointment")
    void shouldUpdateAppointment() {
        var toUpdateDomain = AppointmentStub.createStub(AppointmentEventEnum.CANCELLED_APPOINTMENT);
        var toUpdateEntity = AppointmentStub.createEntity();
        toUpdateEntity.setEvent(AppointmentEventEnum.CANCELLED_APPOINTMENT);

        when(appointmentRepository.findOne(any()))
                .thenReturn(Optional.of(AppointmentStub.createEntity()));
        when(appointmentRepository.save(any()))
                .thenReturn(toUpdateEntity);

        var updatedAppointment = appointmentSpringGateway.update(toUpdateDomain);
        assertNotNull(updatedAppointment);
        assertEquals(AppointmentEventEnum.CANCELLED_APPOINTMENT, updatedAppointment.event());
    }

    @Test
    @DisplayName("should throw when updating non-existing appointment")
    void shouldThrowWhenUpdatingNonExistingAppointment() {
        var toUpdateDomain = AppointmentStub.createStub(AppointmentEventEnum.CANCELLED_APPOINTMENT);

        when(appointmentRepository.findOne(any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            appointmentSpringGateway.update(toUpdateDomain);
        });
    }
}