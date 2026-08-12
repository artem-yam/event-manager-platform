package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.repository.LocationRepository;
import dev.sorokin.eventmanager.validation.ValidationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    @Value("${validation.messages.location.not_found}")
    private String LOCATION_NOT_FOUND_MESSAGE;
    @Value("${validation.messages.location.with_events}")
    private String LOCATION_WITH_EVENTS_MESSAGE;

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

    protected LocationEntity getEntityById(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId)));
    }

    public LocationDto update(Long locationId, LocationDto dto) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId)));
        validationService.validateLocation(countEventPlaces(entity.getEvents()), dto);
        locationMapper.updateEntity(entity, dto);
        locationRepository.save(entity);
        return locationMapper.toDto(entity);
    }

    private Integer countEventPlaces(List<EventEntity> events) {
        return events.stream().mapToInt(EventEntity::getMaxPlaces).sum();
    }

    public void delete(Long locationId) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.formatted(locationId)));
        if (!entity.getEvents().isEmpty()) {
            throw new IllegalArgumentException(LOCATION_WITH_EVENTS_MESSAGE.formatted(locationId));
        }

        locationRepository.deleteById(locationId);
    }
}