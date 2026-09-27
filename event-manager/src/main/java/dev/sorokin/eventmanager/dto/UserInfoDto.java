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
public class UserInfoDto {

    @NotNull
    @Schema(description = "Уникальный идентификатор пользователя", example = "12345")
    private Long id;

    @NotBlank
    @Schema(description = "Уникальный логин пользователя", example = "test_user")
    private String login;

    @NotNull
    @Min(0)
    @Schema(description = "Возраст пользователя", example = "20")
    private Integer age;

    @NotNull
    @Schema(description = "Роль пользователя", example = "USER")
    private UserRole role;

}
