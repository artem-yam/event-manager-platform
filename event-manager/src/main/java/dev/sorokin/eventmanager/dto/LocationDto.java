package dev.sorokin.eventmanager.dto;

import dev.sorokin.eventmanager.entity.LocationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

/**
 * DTO for {@link LocationEntity}
 */
@Value
public class LocationDto {

    @NotNull
    @Schema(description = "Имя локации", example = "КТ Октябрь")
    String name;

    @NotNull
    @Schema(description = "Адрес локации", example = "улица Пушкина")
    String address;

    @NotNull
    @Min(5)
    @Schema(description = "Вместительность локации", example = "1000")
    Integer capacity;

    @Schema(description = "Дополнительное описание локации", example = "Тестовое описание")
    String description;

}