package dev.sorokin.eventnotificator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Изменение одного поля мероприятия")
public class NotificationChange {

    @Schema(description = "Имя изменившегося поля мероприятия", example = "status")
    private String field;

    @Schema(description = "Старое значение поля", nullable = true)
    private Object oldValue;

    @Schema(description = "Новое значение поля", nullable = true)
    private Object newValue;
}