package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.gateway.UserGateway;
import com.fiap.appointmentms.core.stub.AppointmentStub;
import com.fiap.appointmentms.core.stub.UserStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterAppointmentUsecaseTest {

    @Mock
    private AppointmentGateway appointmentGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private AppointmentEventSourcingGateway appointmentEventSourcingGateway;

    @InjectMocks
    private RegisterAppointmentUsecase registerAppointmentUsecase;

    @Test
    @DisplayName("Should get doctor and register an appointment successfully")
    void shouldRegisterAnAppointmentWithSearchDoctor() {
        var doctor = UserStub.createDoctor();
        var patient = UserStub.createPatient();
        var nurse = UserStub.createNurse();
        var appointment = AppointmentStub.createStub(doctor, nurse, patient, LocalDateTime.now().plusDays(1));
        var expectedAppointment = AppointmentStub.createStub(1L, doctor, nurse, patient, LocalDateTime.now().plusDays(1));

        when(userGateway.findByLogin("nurse"))
                .thenReturn(Optional.of(nurse));

        when(userGateway.findById(1L))
                .thenReturn(Optional.of(doctor));

        when(userGateway.findById(3L))
                .thenReturn(Optional.of(patient));

        when(appointmentGateway.save(any()))
                .thenReturn(expectedAppointment);

        registerAppointmentUsecase.execute(appointment);

        verify(userGateway, times(2)).findById(anyLong());
        verify(appointmentEventSourcingGateway, times(1)).registerAppointment(any());
    }

    @Test
    @DisplayName("Should register an appointment successfully")
    void shouldRegisterAnAppointmentWithoutSearchDoctor() {
        var doctor = UserStub.createDoctor();
        var patient = UserStub.createPatient();
        var appointment = AppointmentStub.createStub(doctor, doctor, patient, LocalDateTime.now().plusDays(1));
        var expectedAppointment = AppointmentStub.createStub(1L, doctor, doctor, patient, LocalDateTime.now().plusDays(1));

        when(userGateway.findByLogin("doctor"))
                .thenReturn(Optional.of(doctor));

        when(userGateway.findById(3L))
                .thenReturn(Optional.of(patient));

        when(appointmentGateway.save(any()))
                .thenReturn(expectedAppointment);

        registerAppointmentUsecase.execute(appointment);

        verify(userGateway, times(1)).findById(anyLong());
        verify(appointmentEventSourcingGateway, times(1)).registerAppointment(any());
    }


    @Test
    @DisplayName("should throw IllegalArgumentException when appointment date is before current date")
    void shouldThrowIllegalArgumentExceptionWhenAppointmentDateIsBeforeCurrentDate() {
        var doctor = UserStub.createDoctor();
        var patient = UserStub.createPatient();
        var nurse = UserStub.createNurse();
        var appointment = AppointmentStub.createStub(doctor, nurse, patient, LocalDateTime.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> registerAppointmentUsecase.execute(appointment));
    }
}