package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.LocationRequest;
import dev.sorokin.eventmanager.dto.LocationResponse;
import dev.sorokin.eventmanager.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface LocationMapper {

    List<Location> toEntityList(List<LocationRequest> locationRequest);

    @Mapping(target = "id", ignore = true)
    Location toEntity(LocationRequest locationRequest);

    List<LocationResponse> toDtoList(List<Location> location);

    LocationResponse toDto(Location location);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Location entity, LocationRequest locationRequest);
}