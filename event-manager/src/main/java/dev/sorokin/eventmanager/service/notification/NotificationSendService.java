package dev.sorokin.eventmanager.service.notification;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationSendService {

    private final KafkaTemplate<String, EventChangeKafkaMessage> kafkaTemplate;

    @Value("${spring.kafka.notification-topic}")
    private String topic;

    public void sendMessage(EventChangeKafkaMessage message) {
        log.info("Sending message to topic '{}': {}", topic, message);

        kafkaTemplate.send(topic, message.getMessageId().toString(), message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully. Offset: {}",
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send message: {}", ex.getMessage(), ex);
                    }
                });
    }

}
