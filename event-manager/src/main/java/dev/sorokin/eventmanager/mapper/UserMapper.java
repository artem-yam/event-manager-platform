package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.dto.UserInfoDto;
import dev.sorokin.eventmanager.dto.UserRegistrationDto;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public abstract class UserMapper {

    private static final String LOGIN_VALIDATION_ERROR_MESSAGE = "Логин пользователя не может быть пустым";
    private static final String PASSWORD_VALIDATION_ERROR_MESSAGE = "Пароль пользователя не может быть пустым";
    private static final String AGE_VALIDATION_ERROR_MESSAGE = "Возраст пользователя не может быть пустой или меньше 18";

    public abstract UserInfoDto toDto(UserEntity entity);

    public UserEntity createEntity(UserRegistrationDto userRegistrationDto) {
        validateCreation(userRegistrationDto);
        return createNewEntity(userRegistrationDto);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", constant = "USER")
    protected abstract UserEntity createNewEntity(UserRegistrationDto userRegistrationDto);

    private void validateCreation(UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto.getLogin() == null) {
            throw new IllegalArgumentException(LOGIN_VALIDATION_ERROR_MESSAGE);
        }
        if (userRegistrationDto.getPassword() == null) {
            throw new IllegalArgumentException(PASSWORD_VALIDATION_ERROR_MESSAGE);
        }
        if (userRegistrationDto.getAge() == null || userRegistrationDto.getAge() < 18) {
            throw new IllegalArgumentException(AGE_VALIDATION_ERROR_MESSAGE);
        }
    }
}