package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.api.LocationApiContractIntrf;
import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController implements LocationApiContractIntrf {

    private final LocationService locationService;

    @Override
    public ResponseEntity<List<LocationDto>> getAll() {
        List<LocationDto> locations = locationService.getAll();
        return ResponseEntity.ok(locations);
    }

    @Override
    public ResponseEntity<LocationDto> create(LocationDto request) {
        LocationDto created = locationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<LocationDto> getById(Long locationId) {
        LocationDto location = locationService.getById(locationId);
        return ResponseEntity.ok(location);
    }

    @Override
    public ResponseEntity<LocationDto> update(Long locationId, LocationDto request) {
        LocationDto updated = locationService.update(locationId, request);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long locationId) {
        locationService.delete(locationId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}