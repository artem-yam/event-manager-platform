package dev.sorokin.eventnotificator.service;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.dto.MarkNotificationsAsReadRequest;
import dev.sorokin.eventnotificator.dto.NotificationResponse;
import dev.sorokin.eventnotificator.mapper.NotificationMapper;
import dev.sorokin.eventnotificator.repository.NotificationEventPayloadRepository;
import dev.sorokin.eventnotificator.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationEventPayloadRepository notificationEventPayloadRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    @Transactional
    public void save(EventChangeKafkaMessage msg) {
        var payloadEntity = notificationMapper.createPayloadEntity(msg);
        payloadEntity = notificationEventPayloadRepository.save(payloadEntity);

        var notificationEntities = notificationMapper.createEntities(payloadEntity, msg.getSubscribers());
        notificationRepository.saveAll(notificationEntities);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications() {
        var userId = userService.getActiveUser().getId();
        var userNotifications = notificationRepository.getAllByUserIdAndIsReadFalse(userId);
        return notificationMapper.toNotificationResponseList(userNotifications);
    }

    @Transactional
    public void markNotificationsAsRead(MarkNotificationsAsReadRequest request) {
        var user = userService.getActiveUser();
        var readNotificationsCount = notificationRepository.markAsRead(user.getId(), request.getNotificationIds());
        log.info("Пользователь {} прочитал нотификации в количестве: {}", user.getLogin(), readNotificationsCount);
    }

}
