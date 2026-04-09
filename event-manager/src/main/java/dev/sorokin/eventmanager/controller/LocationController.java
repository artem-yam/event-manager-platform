package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.api.LocationApi;
import dev.sorokin.eventmanager.dto.LocationRequest;
import dev.sorokin.eventmanager.dto.LocationResponse;
import dev.sorokin.eventmanager.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController implements LocationApi {

    private final LocationService locationService;

    @Override
    public ResponseEntity<List<LocationResponse>> getAll() {
        List<LocationResponse> locations = locationService.getAll();
        return ResponseEntity.ok(locations);
    }

    @Override
    public ResponseEntity<LocationResponse> create(LocationRequest request) {
        LocationResponse created = locationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<LocationResponse> getById(Long locationId) {
        LocationResponse location = locationService.getById(locationId);
        return ResponseEntity.ok(location);
    }

    @Override
    public ResponseEntity<LocationResponse> update(Long locationId, LocationRequest request) {
        LocationResponse updated = locationService.update(locationId, request);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> delete(Long locationId) {
        locationService.delete(locationId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}