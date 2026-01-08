package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.stub.AppointmentCancelledStub;
import com.fiap.appointmentms.core.stub.AppointmentStub;
import com.fiap.core.enums.AppointmentEventEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelledAppointmentUsecaseTest {

    @Mock
    private AppointmentGateway appointmentGateway;

    @Mock
    private AppointmentEventSourcingGateway appointmentEventSourcingGateway;


    @InjectMocks
    private CancelledAppointmentUsecase cancelledAppointmentUsecase;

    @Test
    @DisplayName("Should be cancelled appointment")
    void shouldBeCancelledAppointment() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.REGISTERED_APPOINTMENT);
        var cancelledAppointment = AppointmentCancelledStub.createCancelledStub(appointment.id());

         when(appointmentGateway.findById(appointment.id()))
                .thenReturn(Optional.of(appointment));

        when(appointmentGateway.findById(appointment.id()))
                .thenReturn(Optional.of(appointment));

        cancelledAppointmentUsecase.execute(cancelledAppointment);

        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentEventSourcingGateway, times((1))).cancelAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when trying to cancel a non-existing appointment")
    void shouldThrowExceptionWhenCancellingNonExistingAppointment() {
        var cancelledAppointment = AppointmentCancelledStub.createCancelledStub(99L);

        when(appointmentGateway.findById(99L))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cancelledAppointmentUsecase.execute(cancelledAppointment);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(99L);
        verify(appointmentEventSourcingGateway, never()).cancelAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when trying to cancel a finished appointment")
    void shouldThrowExceptionWhenCancellingFinishedAppointment() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.COMPLETED_APPOINTMENT);
        var cancelledAppointment = AppointmentCancelledStub.createCancelledStub(appointment.id());

        when(appointmentGateway.findById(appointment.id()))
                .thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cancelledAppointmentUsecase.execute(cancelledAppointment);
        });

        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentEventSourcingGateway, never()).cancelAppointment(any());
    }

    @Test
    @DisplayName("Should throw exception when trying to cancel an already cancelled appointment")
    void shouldThrowExceptionWhenCancellingAlreadyCancelledAppointment() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.CANCELLED_APPOINTMENT);
        var cancelledAppointment = AppointmentCancelledStub.createCancelledStub(appointment.id());

        when(appointmentGateway.findById(appointment.id()))
                .thenReturn(Optional.of(appointment));
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            cancelledAppointmentUsecase.execute(cancelledAppointment);
        });

        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentEventSourcingGateway, never()).cancelAppointment(any());
    }
}