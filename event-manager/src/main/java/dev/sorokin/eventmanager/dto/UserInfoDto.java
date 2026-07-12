package dev.sorokin.eventmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UserInfoDto {

    @NotNull
    @Schema(description = "Уникальный идентификатор пользователя", example = "12345")
    Long id;

    @NotBlank
    @Schema(description = "Уникальный логин пользователя", example = "test_user")
    String login;

    @NotNull
    @Min(0)
    @Schema(description = "Возраст пользователя", example = "20")
    Integer age;

    @NotNull
    @Schema(description = "Роль пользователя", example = "USER")
    UserRole role;

}
