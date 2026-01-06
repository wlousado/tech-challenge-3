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

    @Override
    @KafkaHandler
    public void doOnRegisterAppointment(AppointmentRegisterMessage message) {
        log.info("Registering appointment: {}", message);
        AppointmentTimeline appointmentTimelineDomain = AppointmentTimelinePresenter.toDomain(message);
        appointmentTimelineGateway.save(appointmentTimelineDomain);

        AppointmentView appointmentViewDomain = AppointmentViewPresenter.toDomain(message, LocalDateTime.now());
        appointmentViewGateway.save(appointmentViewDomain);
    }

    @Override
    @KafkaHandler
    public void doOnUpdateAppointment(AppointmentUpdateMessage message) {
        log.info("Updating appointment: {}", message);
        AppointmentTimeline appointmentTimelineDomain = AppointmentTimelinePresenter.toDomain(message);
        appointmentTimelineGateway.save(appointmentTimelineDomain);

        AppointmentView appointmentViewDomain = AppointmentViewPresenter.toDomain(message, LocalDateTime.now());
        appointmentViewGateway.update(appointmentViewDomain);
    }

    @Override
    @KafkaHandler
    public void doOnCorrectionAppointment(AppointmentCorrectionMessage message) {
        log.info("Correcting appointment: {}", message);
        AppointmentTimeline appointmentTimeline = AppointmentTimelinePresenter.toDomain(message);
        appointmentTimelineGateway.save(appointmentTimeline);

        AppointmentView appointmentView = AppointmentViewPresenter.toDomain(message, LocalDateTime.now());
        appointmentViewGateway.update(appointmentView);
    }

    @Override
    @KafkaHandler
    public void doOnCancelAppointment(AppointmentCancelledMessage message) {
        log.info("Cancel appointment: {}", message);
        AppointmentTimeline appointmentTimeline = AppointmentTimelinePresenter.toDomain(message);
        appointmentTimelineGateway.save(appointmentTimeline);

        AppointmentView appointmentView = AppointmentViewPresenter.toDomain(message, LocalDateTime.now());
        appointmentViewGateway.update(appointmentView);
    }

    @Override
    @KafkaHandler
    public void doOnCompleteAppointment(AppointmentCompletedMessage message) {
        log.info("Complete appointment: {}", message);
        AppointmentTimeline appointmentTimeline = AppointmentTimelinePresenter.toDomain(message);
        appointmentTimelineGateway.save(appointmentTimeline);

        AppointmentView appointmentView = AppointmentViewPresenter.toDomain(message, LocalDateTime.now());
        appointmentViewGateway.update(appointmentView);
    }
}
