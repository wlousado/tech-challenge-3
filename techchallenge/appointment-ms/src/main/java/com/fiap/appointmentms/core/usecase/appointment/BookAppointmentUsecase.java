package com.fiap.appointmentms.core.usecase.appointment;

import com.fiap.appointmentms.core.domain.Appointment;
import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.core.gateway.NotificationGateway;
import com.fiap.appointmentms.core.gateway.UserGateway;
import com.fiap.appointmentms.core.presenter.AppointmentMessagePresenter;
import com.fiap.appointmentms.core.presenter.AppointmentPresenter;
import com.fiap.core.enums.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BookAppointmentUsecase {

    private final UserGateway userGateway;
    private final NotificationGateway notificationGateway;

    public BookAppointmentUsecase(UserGateway userGateway, NotificationGateway notificationGateway) {
        this.userGateway = userGateway;
        this.notificationGateway = notificationGateway;
    }

    public void execute(Appointment appointment) {
        User doctor = null;

        if(appointment.dateTimeOfAppointment().isBefore(LocalDateTime.now())){
            log.error("Data de agendamento deve ser maior que a data atual");
            throw new IllegalArgumentException("Data de agendamento deve ser maior que a data atual");
        }

        var registeredBy = userGateway.findById(appointment.registeredBy().id())
                .orElseThrow();
        var patient = userGateway.findById(appointment.patient().id())
                .orElseThrow();

        if(!registeredBy.type().equals(UserTypeEnum.DOCTOR) && !registeredBy.type().equals(UserTypeEnum.NURSE)) {
            log.error("Usuario sem permissao para realizar agendamento");
            throw new IllegalArgumentException("Usuario sem permissao para realizar agendamento");
        }

        if(registeredBy.id().equals(appointment.doctor().id())) {
            doctor = registeredBy;
        } else {
            doctor = userGateway.findById(appointment.doctor().id())
                    .orElseThrow();
        }

        var register = AppointmentPresenter.toDomain(appointment, doctor, registeredBy, patient);
        var appointmentRegistred = AppointmentMessagePresenter.toMessage(register);
        notificationGateway.registerAppointment(appointmentRegistred);
    }
}
