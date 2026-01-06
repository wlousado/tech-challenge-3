package com.fiap.appointmentms.infra.gateway.spring.message.consumer;

import com.fiap.appointmentms.core.domain.AppointmentTimeline;
import com.fiap.appointmentms.core.domain.AppointmentView;
import com.fiap.appointmentms.core.gateway.AppointmentEventListnerGateway;
import com.fiap.appointmentms.core.gateway.AppointmentTimelineGateway;
import com.fiap.appointmentms.core.gateway.AppointmentViewGateway;
import com.fiap.appointmentms.infra.presenter.AppointmentTimelinePresenter;
import com.fiap.appointmentms.infra.presenter.AppointmentViewPresenter;
import com.fiap.core.message.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@KafkaListener(topics = "appointments-events", groupId = "appointment-notification-group")
@Slf4j
public class AppointmentConsumerSpringGateway implements AppointmentEventListnerGateway {

    private final AppointmentViewGateway appointmentViewGateway;
    private final AppointmentTimelineGateway appointmentTimelineGateway;


    public AppointmentConsumerSpringGateway(AppointmentViewGateway appointmentViewGateway, AppointmentTimelineGateway appointmentTimelineGateway) {
        this.appointmentViewGateway = appointmentViewGateway;
        this.appointmentTimelineGateway = appointmentTimelineGateway;
    }

    @KafkaHandler
    public void doOnRegisterAppointment(AppointmentRegisterMessage message) {
        log.info("Registering appointment: {}", message);
        AppointmentTimeline appointmentTimelineDomain = AppointmentTimelinePresenter.toDomain(message);
        appointmentTimelineGateway.save(appointmentTimelineDomain);

        AppointmentView appointmentViewDomain = AppointmentViewPresenter.toDomain(message, LocalDateTime.now());
        appointmentViewGateway.save(appointmentViewDomain);
    }

    @Override
    public void doOnUpdateAppointment(AppointmentUpdateMessage message) {

    }

    @Override
    public void doOnCorrectionAppointment(AppointmentCorrectionMessage message) {

    }

    @Override
    public void doOnCancelAppointment(AppointmentCancelledMessage message) {

    }

    @Override
    public void doOnCompleteAppointment(AppointmentCompletedMessage message) {

    }
}
