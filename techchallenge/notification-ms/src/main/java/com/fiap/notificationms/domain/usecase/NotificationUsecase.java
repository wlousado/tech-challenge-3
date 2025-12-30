package com.fiap.notificationms.domain.usecase;

import com.fiap.core.message.AppointmentMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationUsecase {
    private final Logger log = LoggerFactory.getLogger(NotificationUsecase.class);
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    public NotificationUsecase(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void execute(AppointmentMessage consulta) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(consulta.paciente().email());
            message.setSubject("Confirmação de Consulta");
            message.setText(buildEmailBody(consulta));

            mailSender.send(message);
            log.info("Notificação enviada para: {}", consulta.paciente().email());
        } catch (Exception e) {
            log.error("Erro ao enviar notificação: {}", e.getMessage());
        }
    }

    private String buildEmailBody(AppointmentMessage consulta) {
        return String.format(
                "Olá, %s!" +
                        "Sua consulta foi agendada com sucesso." +
                        "Detalhes da consulta:" +
                        "Descrição: %s" +
                        "Médico: %s" +
                        "Data: %s" +
                        "Atenciosamente, Equipe de Atendimento",
                consulta.paciente().name(),
                consulta.descricao(),
                consulta.medico().name(),
                consulta.data()
        );
    }
}
