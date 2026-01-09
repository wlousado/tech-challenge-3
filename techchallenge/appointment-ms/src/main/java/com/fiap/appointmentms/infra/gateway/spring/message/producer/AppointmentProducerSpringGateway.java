package com.fiap.appointmentms.infra.gateway.spring.message.producer;

import com.fiap.appointmentms.core.gateway.AppointmentHistoryGateway;
import com.fiap.appointmentms.core.gateway.AppointmentEventSourcingGateway;
import com.fiap.appointmentms.core.gateway.SerializerGateway;
import com.fiap.appointmentms.infra.presenter.AppointmentHistoryPresenter;
import com.fiap.core.message.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentProducerSpringGateway implements AppointmentEventSourcingGateway {

    private static final String TOPIC = "appointments-events";

    private final AppointmentHistoryGateway appointmentHistoryGateway;
    private final SerializerGateway serializerGateway;
    private final KafkaTemplate<Object, Object> kafka;

    public AppointmentProducerSpringGateway(AppointmentHistoryGateway appointmentHistoryGateway, SerializerGateway serializerGateway, KafkaTemplate<Object, Object> kafka) {
        this.appointmentHistoryGateway = appointmentHistoryGateway;
        this.serializerGateway = serializerGateway;
        this.kafka = kafka;
    }

    @Override
    public void registerAppointment(AppointmentRegisterMessage message) {
        var appointmentId = message.idAppointment();
        var serializedAppointment = serializerGateway.serialize(message);
        appointmentHistoryGateway.save(AppointmentHistoryPresenter.toDomain(message, serializedAppointment));
        kafka.send(TOPIC, String.valueOf(appointmentId), message);
    }

    @Override
    public void updateAppointment(AppointmentUpdateMessage message) {
        var appointmentId = message.idAppointment();
        var serializedAppointment = serializerGateway.serialize(message);
        var dateOfChange = LocalDateTime.now();
        appointmentHistoryGateway.save(AppointmentHistoryPresenter.toDomain(message, serializedAppointment, dateOfChange));
        kafka.send(TOPIC, String.valueOf(appointmentId), message);
    }

    @Override
    public void correctionAppointment(AppointmentCorrectionMessage message) {

    }

    @Override
    public void cancelAppointment(AppointmentCancelledMessage message) {

    }

    @Override
    public void completeAppointment(AppointmentCompletedMessage message) {

    }
}
