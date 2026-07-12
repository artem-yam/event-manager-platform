package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.repository.LocationRepository;
import dev.sorokin.eventmanager.validation.ValidationService;
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
    private final ValidationService validationService;

    public List<LocationDto> getAll() {
        return locationMapper.toDtoList(locationRepository.findAll());
    }

    public LocationDto create(LocationDto dto) {
        validationService.validateLocation(dto);
        var entity = locationMapper.createEntity(dto);
        var saved = locationRepository.save(entity);
        return locationMapper.toDto(saved);
    }

    public LocationDto getById(Long locationId) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId)));
        return locationMapper.toDto(entity);
    }

    public LocationDto update(Long locationId, LocationDto dto) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId)));
        validationService.validateLocation(dto);
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