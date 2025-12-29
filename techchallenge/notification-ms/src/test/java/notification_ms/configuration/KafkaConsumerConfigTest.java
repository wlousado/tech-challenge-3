package notification_ms.configuration;

import com.fiap.notificationms.configuration.KafkaConsumerConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerConfigTest {

    private final KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig();

    @Mock
    private ConsumerFactory<Object, Object> consumerFactory;

    @Test
    void consumerFactory_shouldReturnDefaultKafkaConsumerFactory() {
        KafkaProperties properties = new KafkaProperties();
        
        ConsumerFactory<Object, Object> factory = kafkaConsumerConfig.consumerFactory(properties);

        assertNotNull(factory);
    }

    @Test
    void kafkaListenerContainerFactory_shouldConfigureFactoryCorrectly_whenAckModeIsProvided() {
        KafkaProperties properties = new KafkaProperties();
        properties.getListener().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                kafkaConsumerConfig.kafkaListenerContainerFactory(consumerFactory, properties);

        assertNotNull(factory);
        assertEquals(consumerFactory, factory.getConsumerFactory());
        assertEquals(ContainerProperties.AckMode.MANUAL_IMMEDIATE, factory.getContainerProperties().getAckMode());
    }

    @Test
    void kafkaListenerContainerFactory_shouldFallbackToManualAckMode_whenAckModeIsNotProvided() {
        KafkaProperties properties = new KafkaProperties();
        // AckMode is null by default

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
                kafkaConsumerConfig.kafkaListenerContainerFactory(consumerFactory, properties);

        assertNotNull(factory);
        assertEquals(ContainerProperties.AckMode.MANUAL, factory.getContainerProperties().getAckMode());
    }
}
