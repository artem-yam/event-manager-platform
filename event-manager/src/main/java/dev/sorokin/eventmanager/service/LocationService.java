package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.repository.LocationRepository;
import dev.sorokin.eventmanager.validation.ValidationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.sorokin.eventcommon.validation.ValidationMessages.LOCATION_NOT_FOUND_MESSAGE;
import static dev.sorokin.eventcommon.validation.ValidationMessages.LOCATION_WITH_EVENTS_MESSAGE;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final ValidationService validationService;

    @Cacheable(
            cacheNames = "locations",
            key = "'all'"
    )
    public List<LocationDto> getAll() {
        return locationMapper.toDtoList(locationRepository.findAll());
    }

    @CacheEvict(
            cacheNames = "locations",
            allEntries = true
    )
    public LocationDto create(LocationDto dto) {
        validationService.validateLocation(dto);
        var entity = locationMapper.createEntity(dto);
        var saved = locationRepository.save(entity);
        return locationMapper.toDto(saved);
    }

    @Cacheable(
            cacheNames = "locations",
            key = "#locationId"
    )
    public LocationDto getById(Long locationId) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.toString().formatted(locationId)));
        return locationMapper.toDto(entity);
    }

    protected LocationEntity getEntityById(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.toString().formatted(locationId)));
    }

    @CacheEvict(
            cacheNames = "locations",
            allEntries = true
    )
    @Transactional
    public LocationDto update(Long locationId, LocationDto dto) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.toString().formatted(locationId)));
        validationService.validateLocation(countEventPlaces(entity.getEvents()), dto);
        locationMapper.updateEntity(entity, dto);
        locationRepository.save(entity);
        return locationMapper.toDto(entity);
    }

    private Integer countEventPlaces(List<EventEntity> events) {
        return events.stream().mapToInt(EventEntity::getMaxPlaces).sum();
    }

    @CacheEvict(
            cacheNames = "locations",
            allEntries = true
    )
    @Transactional
    public void delete(Long locationId) {
        var entity = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(LOCATION_NOT_FOUND_MESSAGE.toString().formatted(locationId)));
        if (!entity.getEvents().isEmpty()) {
            throw new IllegalArgumentException(LOCATION_WITH_EVENTS_MESSAGE.toString().formatted(locationId));
        }

        locationRepository.deleteById(locationId);
    }
}