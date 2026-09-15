package dev.sorokin.eventnotificator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Пользовательская нотификация о том, что связанное мероприятие было изменено")
public class NotificationResponse {

    @Schema(description = "Уникальный идентификатор пользовательской нотификации")
    private Long notificationId;

    @Schema(description = "Тип нотификации", example = "EVENT_UPDATED")
    private String type;

    @Schema(description = "Идентификатор мероприятия, к которому относится нотификация")
    private Long eventId;

    @Schema(description = "Дата и время создания пользовательской нотификации", format = "date-time")
    private LocalDateTime createdAt;

    @Schema(description = "Флаг прочитанности нотификации")
    private Boolean isRead;

    @Schema(description = "Короткое человеко-читаемое сообщение для быстрого отображения", example = "Событие было изменено")
    private String message;

    @Schema(description = "Структурированный payload с деталями изменения события")
    private NotificationPayload payload;
}
