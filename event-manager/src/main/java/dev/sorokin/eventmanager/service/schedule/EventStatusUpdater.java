package dev.sorokin.eventmanager.service.schedule;

import dev.sorokin.eventmanager.dto.EventStatus;
import dev.sorokin.eventmanager.repository.EventRepository;
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

    private final EventRepository eventRepository;

    @Scheduled(cron = "${event.status.cron}")
    public void updateStatuses() {
        log.info("Запущено обновление статусов событий");

        int updatedToStarted = eventRepository.updateStartedEvents();
        log.info("Событий переведено из {} в {}: {}", EventStatus.WAIT_START, EventStatus.STARTED, updatedToStarted);

        int updatedToFinished = eventRepository.updateFinishedEvents();
        log.info("Событий переведено из {} в {}: {}", EventStatus.STARTED, EventStatus.FINISHED, updatedToFinished);
    }

}
