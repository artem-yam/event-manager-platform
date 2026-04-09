package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.LocationRequest;
import dev.sorokin.eventmanager.dto.LocationResponse;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private static final String LOCATION_NOT_FOUND_MESSAGE = "Не найдена локация с id %s";

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public List<LocationResponse> getAll() {
        return locationMapper.toDtoList(locationRepository.findAll());
    }

    public LocationResponse create(LocationRequest dto) {
        var entity = locationMapper.toEntity(dto);
        var saved = locationRepository.save(entity);
        return locationMapper.toDto(saved);
    }

    public LocationResponse getById(Long locationId) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId)));
        return locationMapper.toDto(entity);
    }

    public LocationResponse update(Long locationId, LocationRequest dto) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId)));
        locationMapper.updateEntity(entity, dto);
        locationRepository.save(entity);
        return locationMapper.toDto(entity);
    }

    public void delete(Long locationId) {
        boolean exists = locationRepository.existsById(locationId);
        if (!exists) {
            throw new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId));
        }

        locationRepository.deleteById(locationId);
    }
}