package dev.sorokin.eventnotificator.service;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.dto.MarkNotificationsAsReadRequest;
import dev.sorokin.eventnotificator.dto.NotificationResponse;
import dev.sorokin.eventnotificator.mapper.NotificationMapper;
import dev.sorokin.eventnotificator.repository.NotificationEventPayloadRepository;
import dev.sorokin.eventnotificator.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
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
        var userId = userService.getActiveUser().getId();
        var userNotifications = notificationRepository.getAllByUserIdAndIsReadFalse(userId);
        userNotifications.stream()
                .filter(notification -> request.getNotificationIds().contains(notification.getId()))
                .forEach(notification -> {
                    notification.setRead(true);
                    notification.setReadAt(LocalDateTime.now());
                });
    }

}
