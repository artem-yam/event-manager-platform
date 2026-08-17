package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
        componentModel = "spring",
        imports = {LocalDateTime.class})
public interface RegistrationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", source = "event", qualifiedByName = "updateOccupiedPlaces")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    RegistrationEntity createNewRegistration(EventEntity event, UserEntity user);

    @Named("updateOccupiedPlaces")
    default EventEntity updateOccupiedPlaces(EventEntity event) {
        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        return event;
    }
}
