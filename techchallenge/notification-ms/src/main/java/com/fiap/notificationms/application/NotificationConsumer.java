package com.fiap.notificationms.application;

import com.fiap.core.dto.ConsultaDto;
import com.fiap.notificationms.domain.usecase.NotificationUsecase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    private final NotificationUsecase notificationUsecase;

    public NotificationConsumer(NotificationUsecase notificationUsecase) {
        this.notificationUsecase = notificationUsecase;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsultaDto consulta, Acknowledgment acknowledgment) {
        log.info("Recebida a consulta para notificação: {}", consulta);
        try {
            notificationUsecase.execute(consulta);
            acknowledgment.acknowledge();
            log.info("Mensagem processada e confirmada com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao processar a mensagem: {}. A mensagem não será confirmada.", consulta, e);
        }
    }
}
