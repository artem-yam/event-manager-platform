package dev.sorokin.eventmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessageResponse {
    private String message;
    private String detailedMessage;
    private LocalDateTime dateTime;
    private Map<String, String> details;

    public ErrorMessageResponse(String message, String detailedMessage) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.dateTime = LocalDateTime.now();
    }
}