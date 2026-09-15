package dev.sorokin.eventnotificator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Структурированный payload изменения события")
public class NotificationPayload {

    @Schema(description = "Технический идентификатор Kafka-сообщения для идемпотентности", format = "uuid")
    private String messageId;

    @Schema(description = "Тип доменного события", example = "EVENT_UPDATED")
    private String eventType;

    @Schema(description = "Дата и время возникновения изменения", format = "date-time")
    private LocalDateTime occurredAt;

    @Schema(description = "Пользователь, который внес изменение. Может быть null, если изменение сделано системой", nullable = true)
    private Long changedById;

    @Schema(description = "Владелец мероприятия")
    private Long ownerId;

    @Schema(description = "Snapshot названия мероприятия на момент сохранения нотификации", example = "Java Meetup")
    private String eventName;

    @Schema(description = "Список изменившихся полей мероприятия")
    private List<NotificationChange> changes;
}