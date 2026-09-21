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
@Schema(description = "Объект данных мероприятия")
public class EventDto {

    @Schema(description = "Уникальный идентификатор мероприятия", example = "42", format = "int64")
    private Long id;

    @Schema(description = "Название мероприятия", example = "Лекция по Java")
    private String name;

    @Schema(description = "id пользователя-создателя мероприятия", example = "10")
    private Long ownerId;

    @Schema(description = "Максимальное кол-во мест на мероприятии", example = "10")
    private Integer maxPlaces;

    @Schema(description = "Кол-во уже занятых мест (создатель не учитывается при подсчете)", example = "7")
    private Integer occupiedPlaces;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Дата и время проведения мероприятия. Формат \"YYYY-MM-DDThh:mm:ss\"", format = "date-time")
    private LocalDateTime date;

    @Schema(description = "Стоимость в рублях", example = "1200", minimum = "1")
    private Integer cost;

    @Schema(description = "Длительность в минутах", example = "60", minimum = "30")
    private Integer duration;

    @Schema(description = "Идентификатор локации, где проходит мероприятие")
    private Long locationId;

    @Schema(description = "Статус мероприятия", implementation = EventStatus.class)
    private EventStatus status;

}
