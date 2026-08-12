package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.entity.LocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface LocationMapper {

    List<LocationDto> toDtoList(List<LocationEntity> locationEntity);

    LocationDto toDto(LocationEntity locationEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    LocationEntity createEntity(LocationDto locationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    void updateEntity(@MappingTarget LocationEntity entity, LocationDto locationDto);

}