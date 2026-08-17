package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.dto.EventRequestDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "startAt", source = "request.date")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "durationMinutes", source = "request.duration")
    @Mapping(target = "occupiedPlaces", constant = "0")
    @Mapping(target = "status", constant = "WAIT_START")
    EventEntity createEntity(EventRequestDto request, UserEntity owner, LocationEntity location);

    List<EventDto> toDtoList(List<EventEntity> entityList);

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "duration", source = "durationMinutes")
    @Mapping(target = "date", source = "startAt")
    @Mapping(target = "cost", source = "cost")
    EventDto toDto(EventEntity eventEntity);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startAt", source = "date")
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "occupiedPlaces", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "durationMinutes", source = "duration")
    void updateEntity(@MappingTarget EventEntity entity, EventRequestDto dto);
}
