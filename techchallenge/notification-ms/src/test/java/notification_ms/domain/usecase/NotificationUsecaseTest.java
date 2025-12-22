package notification_ms.domain.usecase;

import com.fiap.core.dto.ConsultaDto;
import com.fiap.core.dto.UsuarioDto;
import com.fiap.notificationms.domain.usecase.NotificationUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationUsecaseTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationUsecase notificationUsecase;

    private ConsultaDto consultaDto;
    private final String EMAIL_FROM = "noreply@notification.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationUsecase, "emailFrom", EMAIL_FROM);

        UsuarioDto paciente = new UsuarioDto("Maria", "Souza", "maria@email.com", "PACIENTE");
        UsuarioDto medico = new UsuarioDto("Dr. Strange", "Stephen", "strange@email.com", "MEDICO");
        consultaDto = new ConsultaDto("Consulta oftalmológica", paciente, medico, "2023-11-15T14:30:00");
    }

    @Test
    void execute_shouldSendEmail_whenDataIsValid() {
        notificationUsecase.execute(consultaDto);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(EMAIL_FROM, sentMessage.getFrom());
        assertEquals(consultaDto.paciente().email(), Objects.requireNonNull(sentMessage.getTo())[0]);
        assertEquals("Confirmação de Consulta", sentMessage.getSubject());
        
        String expectedBody = String.format(
                "Olá, %s!" +
                        "Sua consulta foi agendada com sucesso." +
                        "Detalhes da consulta:" +
                        "Descrição: %s" +
                        "Médico: %s %s" +
                        "Data: %s" +
                        "Atenciosamente, Equipe de Atendimento",
                consultaDto.paciente().nome(),
                consultaDto.descricao(),
                consultaDto.medico().nome(),
                consultaDto.medico().sobrenome(),
                consultaDto.data()
        );
        assertEquals(expectedBody, sentMessage.getText());
    }

    @Test
    void execute_shouldLogException_whenMailSenderFails() {
        doThrow(new RuntimeException("SMTP Error")).when(mailSender).send(any(SimpleMailMessage.class));

        notificationUsecase.execute(consultaDto);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        // No exception should be thrown as it is caught in the usecase
    }
}
