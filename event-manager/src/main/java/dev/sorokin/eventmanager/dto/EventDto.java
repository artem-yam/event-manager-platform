package dev.sorokin.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Schema(description = "Объект данных мероприятия")
public class EventDto {

    @Schema(description = "Уникальный идентификатор мероприятия", example = "42", format = "int64")
    Long id;

    @Schema(description = "Название мероприятия", example = "Лекция по Java")
    String name;

    @Schema(description = "id пользователя-создателя мероприятия", example = "10")
    Long ownerId;

    @Schema(description = "Максимальное кол-во мест на мероприятии", example = "10")
    Integer maxPlaces;

    @Schema(description = "Кол-во уже занятых мест (создатель не учитывается при подсчете)", example = "7")
    Integer occupiedPlaces;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Дата и время проведения мероприятия. Формат \"YYYY-MM-DDThh:mm:ss\"", format = "date-time")
    LocalDateTime date;

    @Schema(description = "Стоимость в рублях", example = "1200", minimum = "1")
    Integer cost;

    @Schema(description = "Длительность в минутах", example = "60", minimum = "30")
    Integer duration;

    @Schema(description = "Идентификатор локации, где проходит мероприятие")
    Long locationId;

    @Schema(description = "Статус мероприятия", implementation = EventStatus.class)
    EventStatus status;

}
