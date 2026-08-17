package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.UserCredentialsDto;
import dev.sorokin.eventmanager.dto.UserInfoDto;
import dev.sorokin.eventmanager.dto.UserRegistrationDto;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.repository.UserRepository;
import dev.sorokin.eventmanager.security.jwt.JwtService;
import dev.sorokin.eventmanager.validation.ValidationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static dev.sorokin.eventmanager.validation.ValidationMessages.*;

@Service
@RequiredArgsConstructor
@Setter
public class UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationService validationService;

    public String auth(UserCredentialsDto user) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getLogin(), user.getPassword()
                )
        );

        return jwtService.generateToken((User) authentication.getPrincipal());
    }

    public UserInfoDto register(UserRegistrationDto request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            throw new IllegalArgumentException(LOGIN_TAKEN_MESSAGE.toString());
        }

        validationService.validateUser(request);
        var userEntity = userMapper.createNewEntity(request);
        var passwordHash = passwordEncoder.encode(request.getPassword());
        userEntity.setPasswordHash(passwordHash);

        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public UserInfoDto getUserById(Long userId) {
        var entity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_BY_ID_NOT_FOUND_MESSAGE.toString().formatted(userId)));
        return userMapper.toDto(entity);
    }

    protected UserEntity getActiveUser() {
        var userLogin = jwtService.getCurrentUserLogin();
        return userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new EntityNotFoundException(USER_BY_LOGIN_NOT_FOUND_MESSAGE.toString().formatted(userLogin)));
    }
}