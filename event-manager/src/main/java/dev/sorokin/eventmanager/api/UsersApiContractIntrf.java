package dev.sorokin.eventmanager.api;

import dev.sorokin.eventmanager.dto.JwtResponse;
import dev.sorokin.eventmanager.dto.UserCredentialsDto;
import dev.sorokin.eventmanager.dto.UserInfoDto;
import dev.sorokin.eventmanager.dto.UserRegistrationDto;
import dev.sorokin.eventmanager.exception.ErrorMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users",
        description = "User management endpoints")
@RequestMapping("/users")
public interface UsersApiContractIntrf {


    /**
     * Регистрация нового пользователя [доступно без JWT]
     */
    @PostMapping
    @Operation(
            summary = "Регистрация нового пользователя [доступно без JWT]",
            description = """
                    Доступно без авторизации. По умолчанию создается пользователь с ролью USER. Пользователя с ролью ADMIN создать должно быть невозможно. Должна быть проверена уникальность логина. Логины должны быть различные у разных пользователей.
                    
                    Изменения по итерациям:
                    - Итерация 2: endpoint добавлен.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRegistrationDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован",
                            content = @Content(schema = @Schema(implementation = UserInfoDto.class))),
                    @ApiResponse(responseCode = "400", description = "Запрос с невалидными данными",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            },
            security = {}
    )
    ResponseEntity<UserInfoDto> register(@Valid @RequestBody UserRegistrationDto request);

    /**
     * Авторизация пользователя и получение JWT [доступно без JWT]
     */
    @PostMapping("/auth")
    @Operation(
            summary = "Авторизация пользователя и получение JWT [доступно без JWT]",
            description = """
                    Доступно без авторизации. Получение JWT токена по данным пользователя
                    
                    Изменения по итерациям:
                    - Итерация 2: endpoint добавлен.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для авторизации пользователя",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCredentialsDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Авторизация успешна, возвращен JWT",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Запрос с невалидными данными",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            },
            security = {}
    )
    ResponseEntity<JwtResponse> auth(@Valid @RequestBody UserCredentialsDto request);


    /**
     * Получить информацию о пользователе по ID. Allowed roles=[ADMIN]
     */
    @GetMapping("/{userId}")
    @Operation(
            summary = "Получить информацию о пользователе по ID. Allowed roles=[ADMIN]",
            description = """
                    Получение данных пользователя (доступно только админам). Учтите, что пароль не должен возвращаться в теле
                    
                    Изменения по итерациям:
                    - Итерация 2: endpoint добавлен.
                    """,
            parameters = @Parameter(
                    name = "userId",
                    description = "Уникальный идентификатор пользователя",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о пользователе",
                            content = @Content(schema = @Schema(implementation = UserInfoDto.class))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "У пользователя недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Сущность не найдена",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<UserInfoDto> getUserById(@PathVariable Long userId);

}