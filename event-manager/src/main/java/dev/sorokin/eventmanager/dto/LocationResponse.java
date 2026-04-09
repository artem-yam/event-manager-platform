package dev.sorokin.eventmanager.dto;

import dev.sorokin.eventmanager.model.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

/**
 * DTO for {@link Location}
 */
@Value
public class LocationResponse {

    Long id;

    @Schema(example = "КТ Октябрь")
    String name;

    @Schema(example = "улица Пушкина")
    String address;

    @Schema(example = "1000")
    Integer capacity;

    @Schema(example = "Тестовое описание")
    String description;

}