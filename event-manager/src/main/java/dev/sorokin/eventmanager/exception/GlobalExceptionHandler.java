package dev.sorokin.eventmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorMessageResponse> handleValidation(MethodArgumentNotValidException ex) {
//        var body = new ErrorMessageResponse("Bad request", ex.getMessage(), LocalDateTime.now());
//        return ResponseEntity.badRequest().body(body);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Ошибка валидации: {}", ex.getMessage());

        // Собираем все ошибки полей
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "Invalid value"
                ));

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "Ошибка валидации данных",
                "VALIDATION_FAILED"
        );
        errorResponse.setDetails(fieldErrors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Обработка некорректного JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        log.warn("Некорректный JSON в теле запроса: {}", ex.getMessage());

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "Некорректный JSON в теле запроса",
                "INVALID_JSON"
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidation(IllegalArgumentException ex) {
        log.warn("Неверные входные данные: {}", ex.getMessage());

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                ex.getMessage(),
                null
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidation(EntityNotFoundException ex) {
        log.warn("Сущность не найдена: ", ex);

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Обработка всех непредвиденных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleAllExceptions(Exception ex) {
        log.error("Непредвиденная ошибка: ", ex);

        ErrorMessageResponse errorResponse = new ErrorMessageResponse(
                "Внутренняя ошибка сервера",
                "INTERNAL_ERROR"
        );

        return ResponseEntity.internalServerError().body(errorResponse);
    }

}