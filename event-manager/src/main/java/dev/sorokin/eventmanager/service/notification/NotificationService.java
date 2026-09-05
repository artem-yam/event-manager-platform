package dev.sorokin.eventmanager.service.notification;

import dev.sorokin.eventcommon.kafka.ChangeItem;
import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventmanager.dto.EventRequestDto;
import dev.sorokin.eventmanager.dto.EventStatus;
import dev.sorokin.eventmanager.entity.EventEntity;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static dev.sorokin.eventcommon.enums.EventType.EVENT_UPDATED;

@Service
@Setter
@RequiredArgsConstructor
public class NotificationService {

    private static final ChangeItem CHANGE_STATUS_TO_STARTED = new ChangeItem("status", EventStatus.WAIT_START, EventStatus.STARTED);
    private static final ChangeItem CHANGE_STATUS_TO_FINISHED = new ChangeItem("status", EventStatus.STARTED, EventStatus.FINISHED);
    private static final ChangeItem CHANGE_STATUS_TO_CANCELED = new ChangeItem("status", EventStatus.WAIT_START, EventStatus.CANCELLED);

    private final NotificationSendService notificationSendService;

    @Transactional
    public void notifyStatusUpdate(List<EventEntity> eventsStarted, List<EventEntity> eventsFinished) {
        var msgToSend = Stream.concat(
                eventsStarted.stream().map(event -> prepareNotificationMessage(
                        event, Collections.singletonList(CHANGE_STATUS_TO_STARTED), null)),
                eventsFinished.stream().map(event -> prepareNotificationMessage(
                        event, Collections.singletonList(CHANGE_STATUS_TO_FINISHED), null))
        );

        msgToSend.forEach(notificationSendService::sendMessage);
    }

    @Transactional
    public void notifyDelete(EventEntity event, Long changeByUserId) {
        var msgToSend = prepareNotificationMessage(event, Collections.singletonList(CHANGE_STATUS_TO_CANCELED), changeByUserId);

        notificationSendService.sendMessage(msgToSend);
    }

    @Transactional
    public void notifyUpdate(EventEntity oldEvent, EventRequestDto updated, Long changeByUserId) {
        var msgToSend = prepareNotificationMessage(oldEvent, checkEventUpdates(oldEvent, updated), changeByUserId);

        notificationSendService.sendMessage(msgToSend);
    }

    private List<ChangeItem> checkEventUpdates(EventEntity old, EventRequestDto updated) {
        List<ChangeItem> changes = new ArrayList<>();

        if (!Objects.equals(old.getName(), updated.getName())) {
            changes.add(new ChangeItem("name", old.getName(), updated.getName()));
        }
        if (!Objects.equals(old.getMaxPlaces(), updated.getMaxPlaces())) {
            changes.add(new ChangeItem("places", old.getMaxPlaces(), updated.getMaxPlaces()));
        }
        if (!Objects.equals(old.getStartAt(), updated.getDate())) {
            changes.add(new ChangeItem("date", old.getStartAt(), updated.getDate()));
        }
        if (!Objects.equals(old.getCost().intValue(), updated.getCost())) {
            changes.add(new ChangeItem("cost", old.getCost().intValue(), updated.getCost()));
        }
        if (!Objects.equals(old.getDurationMinutes(), updated.getDuration())) {
            changes.add(new ChangeItem("duration", old.getDurationMinutes(), updated.getDuration()));
        }

        return changes;
    }

    private EventChangeKafkaMessage prepareNotificationMessage(EventEntity event, List<ChangeItem> changes, Long changeById) {
        var subs = event.getRegistrations().stream().map(reg -> reg.getUser().getId()).distinct().toList();

        return EventChangeKafkaMessage.builder()
                .messageId(UUID.randomUUID())
                .eventType(EVENT_UPDATED.toString())
                .eventId(event.getId())
                .eventName(event.getName())
                .occurredAt(LocalDateTime.now())
                .ownerId(event.getOwner().getId())
                .changedById(changeById)
                .subscribers(subs)
                .changes(changes)
                .build();
    }

}
