package dev.sorokin.eventmanager.service.schedule;

import dev.sorokin.eventmanager.dto.EventStatus;
import dev.sorokin.eventmanager.service.EventService;
import dev.sorokin.eventmanager.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
@RequiredArgsConstructor
@Slf4j
public class EventStatusUpdater {

    private final EventService eventService;
    private final NotificationService notificationService;

    @Scheduled(cron = "${event.status.cron}")
    public void updateStatuses() {
        log.info("Запущено обновление статусов событий");

        var startedEvents = eventService.startEvents();
        log.info("Событий переведено из {} в {}: {}", EventStatus.WAIT_START, EventStatus.STARTED, startedEvents.size());

        var finishedEvents = eventService.finishEvents();
        log.info("Событий переведено из {} в {}: {}", EventStatus.STARTED, EventStatus.FINISHED, finishedEvents.size());

        notificationService.notifyStatusUpdate(startedEvents, finishedEvents);
    }

}
