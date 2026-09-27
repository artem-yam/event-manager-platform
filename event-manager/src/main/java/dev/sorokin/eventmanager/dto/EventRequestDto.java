package dev.sorokin.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект с данными для создания нового мероприятия")
public class EventRequestDto {

    @NotBlank
    @Schema(description = "Название мероприятия", example = "Лекция по Java")
    private String name;

    @NotNull
    @Positive
    @Schema(description = "Максимальное кол-во мест на мероприятии", example = "10")
    private Integer maxPlaces;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Дата и время проведения мероприятия. Формат \"YYYY-MM-DDThh:mm:ss\"",
            format = "date-time",
            example = "2027-08-08T00:00:00"
    )
    private LocalDateTime date;

    @NotNull
    @Positive
    @Schema(description = "Стоимость в рублях",
            example = "1200",
            minimum = "1")
    private Integer cost;

    @NotNull
    @Min(30)
    @Schema(description = "Длительность в минутах",
            example = "60",
            minimum = "30")
    private Integer duration;

    @NotNull
    @Schema(description = "Идентификатор локации, где проходит мероприятие",
            example = "1")
    private Long locationId;

}
