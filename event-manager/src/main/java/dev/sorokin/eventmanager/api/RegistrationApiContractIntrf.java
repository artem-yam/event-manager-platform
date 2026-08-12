package dev.sorokin.eventmanager.api;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.exception.ErrorMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Registrations",
        description = "User registration and cancellation for events")
@RequestMapping("/events/registrations")
public interface RegistrationApiContractIntrf {

    /**
     * Регистрация пользователя на мероприятие по ID. Allowed roles=[USER]
     */
    @PostMapping("/{eventId}")
    @Operation(
            summary = "Регистрация пользователя на мероприятие по ID. Allowed roles=[USER]",
            description = """
                    Необходимо учесть, что статус мероприятия должен позволять регистрацию. Регистрироваться
                    можно только на событие в статусе `WAIT_START`, если в нем остались свободные места.
                    Для статусов `STARTED`, `CANCELLED` и `FINISHED` регистрация запрещена.
                    Endpoint работает только для роли `USER`: пользователь регистрирует на мероприятие самого себя.
                    Роль `ADMIN` в этом проекте считается сотрудником платформы и не участвует в мероприятиях как посетитель.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    """,
            parameters = @Parameter(
                    name = "eventId",
                    description = "Уникальный идентификатор мероприятия",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная регистрация на мероприятие"),
                    @ApiResponse(responseCode = "400", description = "Запрос с невалидными данными",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
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
    ResponseEntity<Void> register(@PathVariable Long eventId);

    /**
     * Отмена регистрации на мероприятие. Allowed roles=[USER]
     */
    @DeleteMapping("/cancel/{eventId}")
    @Operation(
            summary = "Отмена регистрации на мероприятие. Allowed roles=[USER]",
            description = """
                    Необходимо учесть статус мероприятия. Нельзя отменить регистрацию, если мероприятие уже началось или закончилось.
                    Endpoint работает только для роли `USER`: пользователь отменяет только свою собственную регистрацию.
                    Роль `ADMIN` в этом проекте считается сотрудником платформы и не участвует в мероприятиях как посетитель.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    """,
            parameters = @Parameter(
                    name = "eventId",
                    description = "Уникальный идентификатор мероприятия",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Успешная отмена регистрации на мероприятие"),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Сущность не найдена",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<Void> cancel(@PathVariable Long eventId);

    /**
     * Получение мероприятий, на которые зарегистрирован текущий пользователь. Allowed roles=[USER]
     */
    @GetMapping("/my")
    @Operation(
            summary = "Получение мероприятий, на которые зарегистрирован текущий пользователь. Allowed roles=[USER]",
            description = """
                    Все мероприятия на которые записан текущий пользователь. Мероприятия должны возвращаться все,
                    даже те, которые отменены или закончены. Endpoint относится только к роли `USER`, потому что
                    `ADMIN` в этом проекте не является участником мероприятий.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список мероприятий, на которые пользователь зарегистрирован",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<List<EventDto>> getAllForActiveUser();
}
