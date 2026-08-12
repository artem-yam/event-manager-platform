package dev.sorokin.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Schema(description = "Фильтр для поиска мероприятий. Обязательных полей нет. Если все поля не заданы, то должен вернуться список всех мероприятий")
public class EventSearchRequestDto {

    @Schema(description = "Имя мероприятия для поиска по точному совпадению", example = "Лекция")
    String name;

    @Schema(description = "Минимальная вместимость мероприятия", example = "10")
    Integer placesMin;

    @Schema(description = "Максимальная вместимость мероприятия", example = "100")
    Integer placesMax;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Минимальная дата и время начала мероприятия. Формат \"YYYY-MM-DDThh:mm:ss\"",
            format = "date-time",
            example = "2026-08-08T00:00:00"
    )
    LocalDateTime dateStartAfter;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Максимальная дата и время начала мероприятия. Формат \"YYYY-MM-DDThh:mm:ss\"",
            format = "date-time",
            example = "2027-08-08T00:00:00"
    )
    LocalDateTime dateStartBefore;

    @Schema(description = "Минимальная стоимость участия в рублях", example = "1000")
    Integer costMin;

    @Schema(description = "Максимальная стоимость участия в рублях", example = "5000")
    Integer costMax;

    @Schema(description = "Минимальная длительность мероприятия (в минутах)", example = "60")
    Integer durationMin;

    @Schema(description = "Максимальная длительность мероприятия (в минутах)", example = "120")
    Integer durationMax;

    @Schema(description = "Идентификатор локации мероприятия для фильтрации", example = "1")
    Long locationId;

    @Schema(description = "Статус мероприятия для фильтрации", implementation = EventStatus.class)
    EventStatus eventStatus;

}
