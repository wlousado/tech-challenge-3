package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.stub.AppointmentCorrectionStub;
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

@ExtendWith(MockitoExtension.class)
class CorrectionAppointmentUsecaseTest {

    @Mock
    private AppointmentGateway appointmentGateway;

    @Mock
    private AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    @InjectMocks
    private CorrectionAppointmentUsecase correctionAppointmentUsecase;

    @Test
    @DisplayName("should execute correction appointment successfully")
    void testExecuteSuccess() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.REGISTERED_APPOINTMENT);
        var correction = AppointmentCorrectionStub.createStub();

        when(appointmentGateway.findById(appointment.id()))
                .thenReturn(Optional.of(appointment));

        correctionAppointmentUsecase.execute(correction);

        verify(appointmentGateway, times(1)).findById(1L);
        verify(appointmentGateway, times(1)).update(any());
        verify(appointmentEventSourcingGateway, times((1))).correctionAppointment(any());
    }

    @Test
    @DisplayName("should throw exception when appointment not found")
    void testExecuteAppointmentNotFound() {
        var correction = AppointmentCorrectionStub.createStub();

        when(appointmentGateway.findById(correction.idAppointment()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            correctionAppointmentUsecase.execute(correction);
        });

        assertEquals("Appointment not found", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(correction.idAppointment());
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).correctionAppointment(any());
    }

    @Test
    @DisplayName("should throw exception when appointment already finished")
    void testExecuteAppointmentAlreadyFinished() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.COMPLETED_APPOINTMENT);
        var correction = AppointmentCorrectionStub.createStub();

        when(appointmentGateway.findById(appointment.id()))
                .thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            correctionAppointmentUsecase.execute(correction);
        });

        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(appointment.id());
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).correctionAppointment(any());
    }

    @Test
    @DisplayName("should throw exception when appointment is cancelled")
    void testExecuteAppointmentIsCancelled() {
        var appointment = AppointmentStub.createStub(AppointmentEventEnum.CANCELLED_APPOINTMENT);
        var correction = AppointmentCorrectionStub.createStub();

        when(appointmentGateway.findById(appointment.id()))
                .thenReturn(Optional.of(appointment));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            correctionAppointmentUsecase.execute(correction);
        });

        assertEquals("Appointment already finished", exception.getMessage());
        verify(appointmentGateway, times(1)).findById(appointment.id());
        verify(appointmentGateway, never()).update(any());
        verify(appointmentEventSourcingGateway, never()).correctionAppointment(any());
    }
}