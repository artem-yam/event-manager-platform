package dev.sorokin.eventmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsDto {

    @NotBlank
    @Schema(description = "Логин пользователя для авторизации", example = "test_user")
    private String login;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "test_password")
    private String password;

}