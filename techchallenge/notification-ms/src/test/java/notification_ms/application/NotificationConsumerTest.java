package notification_ms.application;

import com.fiap.core.enums.UserTypeEnum;
import com.fiap.core.message.AppointmentMessage;
import com.fiap.core.message.UserMessage;
import com.fiap.notificationms.application.NotificationConsumer;
import com.fiap.notificationms.domain.usecase.NotificationUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock
    private NotificationUsecase notificationUsecase;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    private AppointmentMessage consultaDto;

    @BeforeEach
    void setUp() {
        UserMessage paciente = new UserMessage(123L,"João", "joao@email.com", UserTypeEnum.PATIENT);
        UserMessage medico = new UserMessage(124L, "Dr. House", "house@email.com", UserTypeEnum.DOCTOR);
        consultaDto = new AppointmentMessage("Consulta de rotina", paciente, medico, "2023-10-10T10:00:00");
    }

    @Test
    void consume_shouldCallExecuteAndAcknowledge_whenExecutionIsSuccessful() {
        doNothing().when(notificationUsecase).execute(consultaDto);

        notificationConsumer.consume(consultaDto, acknowledgment);

        verify(notificationUsecase, times(1)).execute(consultaDto);
        verify(acknowledgment, times(1)).acknowledge();
    }

    @Test
    void consume_shouldLogExceptionAndNotAcknowledge_whenExecutionFails() {
        doThrow(new RuntimeException("Erro no envio")).when(notificationUsecase).execute(consultaDto);

        notificationConsumer.consume(consultaDto, acknowledgment);

        verify(notificationUsecase, times(1)).execute(consultaDto);
        verify(acknowledgment, never()).acknowledge();
    }
}
