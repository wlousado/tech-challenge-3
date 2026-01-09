package notification_ms.domain.usecase;

import com.fiap.core.enums.UserTypeEnum;
import com.fiap.core.message.AppointmentMessage;
import com.fiap.core.message.UserMessage;
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

    private AppointmentMessage consultaDto;
    private final String EMAIL_FROM = "noreply@notification.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationUsecase, "emailFrom", EMAIL_FROM);

        UserMessage paciente = new UserMessage(125L, "Maria", "maria@email.com", UserTypeEnum.PATIENT);
        UserMessage medico = new UserMessage(126L, "Dr. Strange", "strange@email.com", UserTypeEnum.DOCTOR);
        consultaDto = new AppointmentMessage("Consulta oftalmológica", paciente, medico, "2023-11-15T14:30:00");
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
                        "Médico: %s" +
                        "Data: %s" +
                        "Atenciosamente, Equipe de Atendimento",
                consultaDto.paciente().name(),
                consultaDto.descricao(),
                consultaDto.medico().name(),
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
