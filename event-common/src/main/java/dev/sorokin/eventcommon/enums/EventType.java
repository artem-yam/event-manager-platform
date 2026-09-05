package dev.sorokin.eventcommon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    EVENT_UPDATED("Событие было изменено");

    private final String description;
}
