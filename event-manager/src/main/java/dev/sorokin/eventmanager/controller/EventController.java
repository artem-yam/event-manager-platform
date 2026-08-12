package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.api.EventApiContractIntrf;
import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.dto.EventRequestDto;
import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController implements EventApiContractIntrf {

    private final EventService eventService;

    @Override
    public ResponseEntity<EventDto> create(EventRequestDto request) {
        EventDto created = eventService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<EventDto> getById(Long eventId) {
        EventDto event = eventService.getById(eventId);
        return ResponseEntity.ok(event);
    }

    @Override
    public ResponseEntity<EventDto> update(Long eventId, EventRequestDto request) {
        EventDto updated = eventService.update(eventId, request);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long eventId) {
        eventService.delete(eventId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EventDto>> search(EventSearchRequestDto request) {
        List<EventDto> events = eventService.search(request);
        return ResponseEntity.ok(events);
    }

    @Override
    public ResponseEntity<List<EventDto>> getAllForActiveUser() {
        List<EventDto> events = eventService.getAllForActiveUser();
        return ResponseEntity.ok(events);
    }
}
