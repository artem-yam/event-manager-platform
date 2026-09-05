package dev.sorokin.eventnotificator.api;


import dev.sorokin.eventcommon.exception.ErrorMessageResponse;
import dev.sorokin.eventnotificator.dto.MarkNotificationsAsReadRequest;
import dev.sorokin.eventnotificator.dto.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Notifications",
        description = "Read and mark notifications generated from event changes")
@RequestMapping("/notifications")
public interface NotificationsApiContractIntrf {

    /**
     * Получить все непрочитанные нотификации пользователя. Allowed roles=[ADMIN, USER]
     */
    @GetMapping
    @Operation(
            summary = "Получить все непрочитанные нотификации пользователя. Allowed roles=[ADMIN, USER]",
            description = """
                    Получить непрочитанные нотификации текущего пользователя. ID пользователя и его роль извлекаются из JWT токена, выданного сервисом event-manager.
                    
                    В ответе возвращаются именно пользовательские нотификации, а не сырой Kafka DTO. У каждой нотификации есть собственный `notificationId`, короткий `message` и структурированный `payload` с деталями изменения события.
                    
                    Если непрочитанных нотификаций нет, сервис должен вернуть `200 OK` и пустой массив.
                    
                    Изменения по итерациям:
                    - Итерация 4: endpoint добавлен; ответ построен вокруг notification DTO с `notificationId`, `message` и `payload`.
                    """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно возвращается список непрочитанных нотификаций пользователя",
                            content = @Content(schema = @Schema(implementation = NotificationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Запрос с невалидными данными",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "У пользователя недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<List<NotificationResponse>> getUnreadNotifications();

    /**
     * Пометить нотификации как прочитанные. Allowed roles=[ADMIN, USER]
     */
    @PostMapping("/mark-as-read")
    @Operation(
            summary = "Пометить нотификации как прочитанные. Allowed roles=[ADMIN, USER]",
            description = """
                    Пометить переданные нотификации как прочитанные. В запросе используются `notificationId`, полученные из `GET /notifications`.
                    
                    Обновлять нужно только записи текущего пользователя из JWT. Если часть ID не найдена или принадлежит другому пользователю, сервис должен безопасно проигнорировать их и не падать с ошибкой.
                    
                    Изменения по итерациям:
                    - Итерация 4: endpoint добавлен.
                    - Итерация 5: изменена внутренняя логика: после mark-as-read добавлена синхронизация unread-counter в Redis с фактическим значением из БД.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Список ID нотификаций для пометки как прочитанные",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MarkNotificationsAsReadRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Нотификации успешно помечены как прочитанные"),
                    @ApiResponse(responseCode = "400", description = "Запрос с невалидными данными",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Аутентификация отсутствует или не удалась",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "403", description = "У пользователя недостаточно прав",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                            content = @Content(schema = @Schema(implementation = ErrorMessageResponse.class)))
            }
    )
    ResponseEntity<Void> markNotificationsAsRead(@Valid @RequestBody MarkNotificationsAsReadRequest request);
}