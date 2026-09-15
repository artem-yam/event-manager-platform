package dev.sorokin.eventnotificator.service;

import dev.sorokin.eventcommon.user.UserBaseEntity;
import dev.sorokin.eventnotificator.repository.UserRepository;
import dev.sorokin.eventnotificator.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import static dev.sorokin.eventcommon.validation.ValidationMessages.USER_BY_LOGIN_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Setter
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    protected UserBaseEntity getActiveUser() {
        var userLogin = jwtService.getCurrentUserLogin();
        return userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new EntityNotFoundException(USER_BY_LOGIN_NOT_FOUND_MESSAGE.toString().formatted(userLogin)));
    }
}