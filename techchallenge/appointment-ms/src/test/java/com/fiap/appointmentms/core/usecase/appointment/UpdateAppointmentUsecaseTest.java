package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.stub.AppointmentStub;
import com.fiap.appointmentms.core.stub.AppointmentUpdateStub;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateAppointmentUsecaseTest {

    @Mock
    private AppointmentGateway appointmentGateway;

    @Mock
    private AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    @InjectMocks
    private UpdateAppointmentUsecase updateAppointmentUsecase;

    @Test
    @DisplayName("Should throw exception when trying to update a non-existing appointment")
    void shouldThrowExceptionWhenUpdatingNonExistingAppointment() {
        var updateAppointment = AppointmentUpdateStub.createUpdateStub(99L);

        when(appointmentGateway.findById(99L))
                .thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            updateAppointmentUsecase.execute(updateAppointment);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(99L);
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).updateAppointment(any());
    }

    @Test
    @DisplayName("Should update an existing appointment successfully")
    void shouldUpdateExistingAppointmentSuccessfully() {
        var existingAppointment = AppointmentStub.createStub(AppointmentEventEnum.REGISTERED_APPOINTMENT);
        var updateAppointment = AppointmentUpdateStub.createUpdateStubExpected(1L);

        when(appointmentGateway.findById(1L))
                .thenReturn(Optional.of(existingAppointment));

        updateAppointmentUsecase.execute(updateAppointment);

        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentEventSourcingGateway, times(1)).updateAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when trying to update a finished appointment")
    void shouldThrowExceptionWhenUpdatingFinishedAppointment() {
        var finishedAppointment = AppointmentStub.createStub(AppointmentEventEnum.COMPLETED_APPOINTMENT);
        var updateAppointment = AppointmentUpdateStub.createUpdateStubExpected(1L);

        when(appointmentGateway.findById(1L))
                .thenReturn(Optional.of(finishedAppointment));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            updateAppointmentUsecase.execute(updateAppointment);
        });

        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).updateAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when trying to update a cancelled appointment")
    void shouldThrowExceptionWhenUpdatingCancelledAppointment() {
        var cancelledAppointment = AppointmentStub.createStub(AppointmentEventEnum.CANCELLED_APPOINTMENT);
        var updateAppointment = AppointmentUpdateStub.createUpdateStubExpected(1L);

        when(appointmentGateway.findById(1L))
                .thenReturn(Optional.of(cancelledAppointment));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            updateAppointmentUsecase.execute(updateAppointment);
        });

        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).updateAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when trying to update an appointment not existing")
    void shouldThrowExceptionWhenUpdatingAppointmentNotExisting() {
        var updateAppointment = AppointmentUpdateStub.createUpdateStubExpected(2L);
        when(appointmentGateway.findById(2L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            updateAppointmentUsecase.execute(updateAppointment);
        });
        assertEquals("Appointment not found", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(2L);
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).updateAppointment(any());
    }
}