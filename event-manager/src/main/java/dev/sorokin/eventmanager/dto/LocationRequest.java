package dev.sorokin.eventmanager.dto;

import dev.sorokin.eventmanager.model.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

/**
 * DTO for {@link Location}
 */
@Value
public class LocationRequest {

    @NotNull
    @Schema(example = "КТ Октябрь")
    String name;

    @NotNull
    @Schema(example = "улица Пушкина")
    String address;

    @NotNull
    @Min(5)
    @Schema(example = "1000")
    Integer capacity;

    @Schema(example = "Тестовое описание")
    String description;

}