package dev.sorokin.eventnotificator.controller;

import dev.sorokin.eventnotificator.api.NotificationsApiContractIntrf;
import dev.sorokin.eventnotificator.dto.MarkNotificationsAsReadRequest;
import dev.sorokin.eventnotificator.dto.NotificationResponse;
import dev.sorokin.eventnotificator.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationsController implements NotificationsApiContractIntrf {

    private final NotificationService notificationService;

    @Override
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        return ResponseEntity.ok(notificationService.getUnreadNotifications());
    }

    @Override
    public ResponseEntity<Void> markNotificationsAsRead(MarkNotificationsAsReadRequest request) {
        notificationService.markNotificationsAsRead(request);
        return ResponseEntity.noContent().build();
    }
}