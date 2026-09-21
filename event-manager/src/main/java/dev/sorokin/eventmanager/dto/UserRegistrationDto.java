package dev.sorokin.eventmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank
    @Schema(description = "Логин пользователя. Должен быть уникальным", example = "test_user")
    private String login;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "test_password")
    private String password;

    @NotNull
    @Min(18)
    @Schema(description = "Возраст пользователя", example = "20")
    private Integer age;

}