package dev.sorokin.eventnotificator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Запрос на пометку списка нотификаций как прочитанных")
public class MarkNotificationsAsReadRequest {

    @NotEmpty(message = "Список ID нотификаций не должен быть пустым")
    @Schema(description = "Список ID нотификаций, которые необходимо пометить как прочитанные", example = "[1,2]")
    private List<Long> notificationIds;
}