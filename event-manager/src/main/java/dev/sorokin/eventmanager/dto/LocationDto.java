package dev.sorokin.eventmanager.dto;

import dev.sorokin.eventmanager.entity.LocationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link LocationEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {

    @NotBlank
    @Schema(description = "Имя локации", example = "КТ Октябрь")
    private String name;

    @NotBlank
    @Schema(description = "Адрес локации", example = "улица Пушкина")
    private String address;

    @NotNull
    @Min(5)
    @Schema(description = "Вместительность локации", example = "1000")
    private Integer capacity;

    @Schema(description = "Дополнительное описание локации", example = "Тестовое описание")
    private String description;

}