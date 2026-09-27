package dev.sorokin.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Фильтр для поиска мероприятий. Обязательных полей нет. Если все поля не заданы, то должен вернуться список всех мероприятий")
public class EventSearchRequestDto {

    @Schema(description = "Имя мероприятия для поиска по точному совпадению", example = "Лекция")
    private String name;

    @Schema(description = "Минимальная вместимость мероприятия", example = "10")
    private Integer placesMin;

    @Schema(description = "Максимальная вместимость мероприятия", example = "100")
    private Integer placesMax;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Минимальная дата и время начала мероприятия. Формат \"YYYY-MM-DDThh:mm:ss\"",
            format = "date-time",
            example = "2026-08-08T00:00:00"
    )
    private LocalDateTime dateStartAfter;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Максимальная дата и время начала мероприятия. Формат \"YYYY-MM-DDThh:mm:ss\"",
            format = "date-time",
            example = "2027-08-08T00:00:00"
    )
    private LocalDateTime dateStartBefore;

    @Schema(description = "Минимальная стоимость участия в рублях", example = "1000")
    private Integer costMin;

    @Schema(description = "Максимальная стоимость участия в рублях", example = "5000")
    private Integer costMax;

    @Schema(description = "Минимальная длительность мероприятия (в минутах)", example = "60")
    private Integer durationMin;

    @Schema(description = "Максимальная длительность мероприятия (в минутах)", example = "120")
    private Integer durationMax;

    @Schema(description = "Идентификатор локации мероприятия для фильтрации", example = "1")
    private Long locationId;

    @Schema(description = "Статус мероприятия для фильтрации", implementation = EventStatus.class)
    private EventStatus eventStatus;

}
