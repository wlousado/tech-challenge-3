package com.fiap.appointmentms.infra.gateway.spring.message;

import com.fiap.appointmentms.core.gateway.AppointmentGateway;
import com.fiap.appointmentms.core.gateway.NotificationGateway;
import com.fiap.appointmentms.infra.presenter.AppointmentPresenter;
import com.fiap.core.message.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationSpringGateway implements NotificationGateway {

    private static final String TOPIC = "appointments-events";

    private final AppointmentGateway appointmentGateway;
    private final KafkaTemplate<Object, Object> kafka;

    public NotificationSpringGateway(AppointmentGateway appointmentGateway, KafkaTemplate<Object, Object> kafka) {
        this.appointmentGateway = appointmentGateway;
        this.kafka = kafka;
    }


    @Override
    public void registerAppointment(RegisterAppointmentMessage message) {
        var appointmentSaved = appointmentGateway.save(AppointmentPresenter.toDomain(message));
        kafka.send(TOPIC, String.valueOf(appointmentSaved.id()), message.addIdAppointment(appointmentSaved.id()));
    }

    @Override
    public void updateAppointment(UpdateAppointmentMessage message) {

    }

    @Override
    public void correctionAppointment(CorrectionAppointmentMessage message) {

    }

    @Override
    public void cancelAppointment(CancelledAppointmentMessage message) {

    }

    @Override
    public void completeAppointment(CompletedAppointmentMessage message) {

    }
}
