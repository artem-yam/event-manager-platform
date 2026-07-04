package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.UserCredentialsDto;
import dev.sorokin.eventmanager.dto.UserInfoDto;
import dev.sorokin.eventmanager.dto.UserRegistrationDto;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.repository.UserRepository;
import dev.sorokin.eventmanager.security.jwt.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "Не найден пользователь с id %s";
    private static final String LOGIN_TAKEN_MESSAGE = "Пользователь с таким логином уже существует";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
            throw new IllegalArgumentException(LOGIN_TAKEN_MESSAGE);
        }

        var userEntity = userMapper.createEntity(request);
        var passwordHash = passwordEncoder.encode(request.getPassword());
        userEntity.setPasswordHash(passwordHash);

        userRepository.save(userEntity);
        return userMapper.toDto(userEntity);
    }

    public UserInfoDto getUserById(Long userId) {
        var entity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(userId)));
        return userMapper.toDto(entity);
    }
}