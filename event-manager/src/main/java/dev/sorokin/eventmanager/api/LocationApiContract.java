package dev.sorokin.eventmanager.api;

import dev.sorokin.eventmanager.dto.LocationDto;
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

@Tag(name = "Locations",
        description = "Location CRUD and capacity constraints")
@RequestMapping("/locations")
public interface LocationApiContract {

    /**
     * Получить список всех локаций
     */
    @GetMapping
    @Operation(
            summary = "Получить список всех локаций. Allowed roles=[ADMIN, USER]",
            description = """
                    Получение списка всех локаций.
                    
                    Изменения по итерациям:
                    - Итерация 1: добавить endpoint для получения списка локаций.
                    - Итерация 2: endpoint теперь вызывается только с Bearer JWT; доступ разрешен ролям ADMIN и USER.
                    - Итерация 5: изменена внутренняя логика чтения; используется cache-aside для списка локаций с Redis.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список всех локаций",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationDto.class)))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "У пользователя недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<List<LocationDto>> getAll();

    /**
     * Создать новую локацию
     */
    @PostMapping
    @Operation(
            summary = "Создать новую локацию. Allowed roles=[ADMIN]",
            description = """
                    Создать новую локацию. Id должен задаваться на стороне приложения (базой данных).
                    
                    Изменения по итерациям:
                    - Итерация 1: добавить endpoint для создания локации с валидацией request body и возвратом созданной сущности.
                    - Итерация 2: endpoint теперь вызывается только с Bearer JWT; доступ разрешен только роли ADMIN.
                    - Итерация 5: изменена внутренняя логика после записи; добавлена инвалидация кешей локаций.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные новой локации",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LocationDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Место успешно создано",
                            content = @Content(schema = @Schema(implementation = LocationDto.class))),
                    @ApiResponse(responseCode = "400", description = "Запрос с невалидными данными",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<LocationDto> create(@Valid @RequestBody LocationDto request);

    /**
     * Получить локацию по ID
     */
    @GetMapping("/{locationId}")
    @Operation(
            summary = "Получить локацию по ID. Allowed roles=[ADMIN, USER]",
            description = """
                    Получить локацию по идентификатору.
                    
                    Изменения по итерациям:
                    - Итерация 1: добавить endpoint для получения локации по locationId. Если локация не найдена, должен возвращаться 404.
                    - Итерация 2: endpoint теперь вызывается только с Bearer JWT; доступ разрешен ролям ADMIN и USER.
                    - Итерация 5: изменена внутренняя логика чтения; используется cache-aside для локации по id с Redis.
                    """,
            parameters = @Parameter(
                    name = "locationId",
                    description = "Уникальный идентификатор местоположения",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "42",
                    schema = @Schema(type = "integer", format = "int64")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный ответ",
                            content = @Content(schema = @Schema(implementation = LocationDto.class))),
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
    ResponseEntity<LocationDto> getById(@PathVariable Long locationId);

    /**
     * Обновить локацию по ID
     */
    @PutMapping("/{locationId}")
    @Operation(
            summary = "Обновить локацию. Allowed roles=[ADMIN]",
            description = """
                    Обновить локацию по идентификатору.
                    
                    Изменения по итерациям:
                    - Итерация 1: добавить endpoint для обновления локации с валидацией request body. Если локация не найдена, должен возвращаться 404.
                    - Итерация 2: endpoint теперь вызывается только с Bearer JWT; доступ разрешен только роли ADMIN.
                    - Итерация 3: добавить ограничение; нельзя уменьшать capacity, если это нарушает ограничения уже существующих мероприятий на этой локации. В этом случае должен возвращаться 400.
                    - Итерация 5: изменена внутренняя логика после обновления; добавлена инвалидация кешей локаций.
                    """,
            parameters = @Parameter(
                    name = "locationId",
                    description = "Уникальный идентификатор местоположения",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "42",
                    schema = @Schema(type = "integer", format = "int64")
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новые данные локации",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LocationDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Место успешно обновлено",
                            content = @Content(schema = @Schema(implementation = LocationDto.class))),
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
    ResponseEntity<LocationDto> update(@PathVariable Long locationId, @Valid @RequestBody LocationDto request);

    /**
     * Удалить локацию по ID
     */
    @DeleteMapping("/{locationId}")
    @Operation(
            summary = "Удалить локацию. Allowed roles=[ADMIN]",
            description = """
                    Удалить локацию по идентификатору.
                    
                    Изменения по итерациям:
                    - Итерация 1: добавить endpoint для удаления локации по locationId. Если локация не найдена, должен возвращаться 404.
                    - Итерация 2: endpoint теперь вызывается только с Bearer JWT; доступ разрешен только роли ADMIN.
                    - Итерация 3: добавить ограничение; нельзя удалять локацию, если на ней уже есть мероприятия. В этом случае должен возвращаться 400.
                    - Итерация 5: изменена внутренняя логика после удаления; добавлена инвалидация кешей локаций.
                    """,
            parameters = @Parameter(
                    name = "locationId",
                    description = "Уникальный идентификатор местоположения",
                    required = true,
                    in = ParameterIn.PATH,
                    example = "42",
                    schema = @Schema(type = "integer", format = "int64")
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Место успешно удалено"),
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
    ResponseEntity<Void> delete(@PathVariable Long locationId);
}