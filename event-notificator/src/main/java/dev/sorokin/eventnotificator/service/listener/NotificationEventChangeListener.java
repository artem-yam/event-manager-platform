package dev.sorokin.eventnotificator.service.listener;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventChangeListener {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${spring.kafka.notification-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "containerFactory"
    )
    public void listenEvents(ConsumerRecord<Long, EventChangeKafkaMessage> record) {
        log.info("Прочитано сообщение об изменении события: {}", record.value());
        notificationService.save(record.value());
    }

}
