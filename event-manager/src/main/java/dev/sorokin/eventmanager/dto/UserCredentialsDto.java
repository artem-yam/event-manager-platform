package dev.sorokin.eventmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UserCredentialsDto {

    @NotNull
    @Schema(description = "Логин пользователя для авторизации", example = "test_user")
    String login;

    @NotNull
    @Schema(description = "Пароль пользователя", example = "test_password")
    String password;

}