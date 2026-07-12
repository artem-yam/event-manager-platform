package dev.sorokin.eventmanager.validation;

import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.dto.UserRegistrationDto;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Setter
public class ValidationService {

    @Value("${validation.messages.empty_location_name}")
    private String NAME_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.empty_location_address}")
    private String ADDRESS_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.empty_location_capacity}")
    private String CAPACITY_VALIDATION_ERROR_MESSAGE;

    @Value("${validation.messages.empty_user_login}")
    private String LOGIN_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.empty_user_password}")
    private String PASSWORD_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.empty_user_age}")
    private String AGE_VALIDATION_ERROR_MESSAGE;

    public void validateLocation(LocationDto locationDto) {
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

    public void validateUser(UserRegistrationDto userRegistrationDto) {
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
