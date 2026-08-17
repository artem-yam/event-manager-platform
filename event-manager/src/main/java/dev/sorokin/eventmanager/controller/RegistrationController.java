package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.api.RegistrationApiContractIntrf;
import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RegistrationController implements RegistrationApiContractIntrf {

    private final RegistrationService registrationService;

    @Override
    public ResponseEntity<Void> register(Long eventId) {
        registrationService.register(eventId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> cancel(Long eventId) {
        registrationService.cancel(eventId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EventDto>> getAllForActiveUser() {
        List<EventDto> events = registrationService.getAllForActiveUser();
        return ResponseEntity.ok(events);
    }
}
