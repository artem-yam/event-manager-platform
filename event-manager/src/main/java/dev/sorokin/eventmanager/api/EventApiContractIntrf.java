package dev.sorokin.eventmanager.api;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.dto.EventRequestDto;
import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.exception.ErrorMessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Events",
        description = "Event CRUD, search and ownership rules")
@RequestMapping("/events")
public interface EventApiContractIntrf {

    /**
     * Создание нового мероприятия. Allowed roles=[USER]
     */
    @PostMapping
    @Operation(
            summary = "Создание нового мероприятия. Allowed roles=[USER]",
            description = """
                    Учтите валидацию всех полей. Все поля обязательные при создании, также должна быть соблюдена логика (например время не может быть в прошлом, стоимость > 0 и т.д.) Локация должна существовать и ее вместимость должна позволять провести это мероприятие, т.е. кол-во мест должно быть достаточно для всех участников. Организатор (создатель) мероприятия не учитывается как участник.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания мероприятия",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Мероприятие успешно создано. Возвращается созданное событие.",
                            content = @Content(schema = @Schema(implementation = EventDto.class))),
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
    ResponseEntity<EventDto> create(@Valid @RequestBody EventRequestDto request);

    /**
     * Получение мероприятия по ID. Allowed roles=[USER, ADMIN]
     */
    @GetMapping("/{eventId}")
    @Operation(
            summary = "Получение мероприятия по ID. Allowed roles=[USER, ADMIN]",
            description = """
                    Предусмотрите обработку ошибок, если мероприятие не найдено.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    - Итерация 5: изменена внутренняя логика чтения: используется cache-aside для события по id с Redis; кеш также инвалидируется при смене статуса события scheduler-ом.
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
                    @ApiResponse(responseCode = "200", description = "Данные мероприятия",
                            content = @Content(schema = @Schema(implementation = EventDto.class))),
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
    ResponseEntity<EventDto> getById(@PathVariable Long eventId);

    /**
     * Обновление мероприятия. Allowed roles=[USER, ADMIN]
     */
    @PutMapping("/{eventId}")
    @Operation(
            summary = "Обновление мероприятия. Allowed roles=[USER, ADMIN]",
            description = """
                    Доступно только ADMIN, либо создателю мероприятия. Т.е. роль из JWT токена должна быть ADMIN, либо userId должен быть равен создателю меропрития. Учтите то, как можно менять мероприятие. Валидируйте входные значения, например maxPlaces должно быть больше, чем уже записанных пользователей, стоимость > 0, длительность > 0 и т.д.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    - Итерация 4: при изменении события добавлена публикация в Kafka доменного события по общему контракту итерации 4: `messageId`, `eventType`, `eventId`, `occurredAt`, `ownerId`, `changedById`, `subscribers`, `changes[]`. Если изменение инициировано не пользователем, `changedById` может быть `null`.
                    - Итерация 5: изменена внутренняя логика после обновления: добавлена инвалидация кеша события по id.
                    """,
            parameters = @Parameter(
                    name = "eventId",
                    description = "Уникальный идентификатор мероприятия",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления мероприятия",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Обновленное мероприятие",
                            content = @Content(schema = @Schema(implementation = EventDto.class))),
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
    ResponseEntity<EventDto> update(@PathVariable Long eventId, @Valid @RequestBody EventRequestDto request);

    /**
     * Удаление мероприятия. Allowed roles=[USER, ADMIN]
     */
    @DeleteMapping("/{eventId}")
    @Operation(
            summary = "Удаление мероприятия. Allowed roles=[USER, ADMIN]",
            description = """
                    Доступно только ADMIN, либо создателю мероприятия. Т.е. роль из JWT токена должна быть ADMIN, либо userId должен быть равен создателю меропрития. Должно быть реализовано "мягкое(soft) удаление" - на самом деле строка в БД не удалется, только меняется статус мероприятия на CANCELLED. При этом нужно учесть то, что не каждое мероприятие можно отменить (можно отменить только те мероприятия, которые еще не начались).
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    - Итерация 4: при отмене события добавлена публикация в Kafka доменного события по общему контракту итерации 4: `messageId`, `eventType`, `eventId`, `occurredAt`, `ownerId`, `changedById`, `subscribers`, `changes[]`; отмена отражается как изменение поля `status`.
                    - Итерация 5: изменена внутренняя логика после отмены события: добавлена инвалидация кеша события по id.
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
                    @ApiResponse(responseCode = "204", description = "Мероприятие успешно удалено"),
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
    ResponseEntity<Void> delete(@PathVariable Long eventId);

    /**
     * Поиск мероприятий по фильтру. Allowed roles=[USER, ADMIN]
     */
    @PostMapping("/search")
    @Operation(
            summary = "Поиск мероприятий по фильтру. Allowed roles=[USER, ADMIN]",
            description = """
                    Поиск мероприятий по фильтру. Если все поля пустые, должен вернуться список всех мероприятий.
                    Поддержите весь набор фильтров из `EventSearchRequestDto`: имя, диапазоны по местам,
                    дате старта, стоимости, длительности, а также `locationId` и `eventStatus`.
                    Для поля `name` используйте точное совпадение, а не поиск по подстроке.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Фильтр для поиска мероприятий",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EventSearchRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список мероприятий, соответствующих заданным фильтрам",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<List<EventDto>> search(@Valid @RequestBody EventSearchRequestDto request);

    /**
     * Получение всех мероприятий, созданных текущим пользователем. Allowed roles=[USER]
     */
    @GetMapping("/my")
    @Operation(
            summary = "Получение всех мероприятий, созданных текущим пользователем. Allowed roles=[USER]",
            description = """
                    Все мероприятия созданные пользователем, который выполняет запрос.
                    
                    Изменения по итерациям:
                    - Итерация 3: endpoint добавлен.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список мероприятий, созданных пользователем",
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
