package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.UserInfoDto;
import dev.sorokin.eventmanager.dto.UserRegistrationDto;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface UserMapper {

    UserInfoDto toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "registrations", ignore = true)
    @Mapping(target = "ownedEvents", ignore = true)
    @Mapping(target = "role", constant = "USER")
    UserEntity createNewEntity(UserRegistrationDto userRegistrationDto);

}