package dev.sorokin.eventmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UserRegistrationDto {

    @NotNull
    @Schema(description = "Логин пользователя. Должен быть уникальным", example = "test_user")
    String login;

    @NotNull
    @Schema(description = "Пароль пользователя", example = "test_password")
    String password;

    @NotNull
    @Min(18)
    @Schema(description = "Возраст пользователя", example = "20")
    Integer age;

}