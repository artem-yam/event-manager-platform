package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.entity.LocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public abstract class LocationMapper {

    private static final String NAME_VALIDATION_ERROR_MESSAGE = "Название локации не может быть пустым";
    private static final String ADDRESS_VALIDATION_ERROR_MESSAGE = "Адрес локации не может быть пустым";
    private static final String CAPACITY_VALIDATION_ERROR_MESSAGE = "Вместительность локации не может быть пустой или меньше 5";

    public abstract List<LocationDto> toDtoList(List<LocationEntity> locationEntity);

    public abstract LocationDto toDto(LocationEntity locationEntity);

    public LocationEntity createEntity(LocationDto locationDto) {
        validateLocation(locationDto);
        LocationEntity entity = new LocationEntity();
        updateEntityFromDto(entity, locationDto);
        return entity;
    }

    public LocationEntity updateEntity(LocationEntity entity, LocationDto locationDto) {
        validateLocation(locationDto);
        updateEntityFromDto(entity, locationDto);
        return entity;
    }

    @Mapping(target = "id", ignore = true)
    protected abstract void updateEntityFromDto(@MappingTarget LocationEntity entity, LocationDto locationDto);

    private void validateLocation(LocationDto locationDto) {
        if (locationDto.getName() == null) {
            throw new IllegalArgumentException(NAME_VALIDATION_ERROR_MESSAGE);
        }
        if (locationDto.getAddress() == null) {
            throw new IllegalArgumentException(ADDRESS_VALIDATION_ERROR_MESSAGE);
        }
        if (locationDto.getCapacity() == null || locationDto.getCapacity() < 5) {
            throw new IllegalArgumentException(CAPACITY_VALIDATION_ERROR_MESSAGE);
        }
    }
}