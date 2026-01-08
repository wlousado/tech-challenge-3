package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.stub.AppointmentStub;
import com.fiap.core.enums.AppointmentEventEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompletedAppointmentUsecaseTest {

    @Mock
    private AppointmentGateway appointmentGateway;

    @Mock
    private AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    @InjectMocks
    private CompletedAppointmentUsecase completedAppointmentUsecase;


    @Test
    @DisplayName("Should complete appointment successfully")
    void testExecute_Success() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.REGISTERED_APPOINTMENT);

        when(appointmentGateway.findById(1L))
                .thenReturn(Optional.of(appointment));


        completedAppointmentUsecase.execute(1L);

        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentGateway, times(1)).update(any());
        verify(appointmentEventSourcingGateway, times(1)).completeAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when appointment not found")
    void testExecute_AppointmentNotFound() {
        when(appointmentGateway.findById(1L))
                .thenReturn(Optional.empty());


        Exception exception = assertThrows(IllegalStateException.class, () -> {
            completedAppointmentUsecase.execute(1L);
        });

        assertEquals("Appointment not found", exception.getMessage());

        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).completeAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when appointment already finished")
    void testExecute_AppointmentAlreadyFinished() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.COMPLETED_APPOINTMENT);

        when(appointmentGateway.findById(1L))
                .thenReturn(Optional.of(appointment));
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            completedAppointmentUsecase.execute(1L);
        });
        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).completeAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when appointment is cancelled")
    void testExecute_AppointmentCancelled() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.CANCELLED_APPOINTMENT);

        when(appointmentGateway.findById(1L))
                .thenReturn(Optional.of(appointment));
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            completedAppointmentUsecase.execute(1L);
        });
        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).completeAppointment(any());
    }
}