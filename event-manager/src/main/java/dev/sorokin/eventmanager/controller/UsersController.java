package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.api.UsersApiContractIntrf;
import dev.sorokin.eventmanager.dto.JwtResponse;
import dev.sorokin.eventmanager.dto.UserCredentialsDto;
import dev.sorokin.eventmanager.dto.UserInfoDto;
import dev.sorokin.eventmanager.dto.UserRegistrationDto;
import dev.sorokin.eventmanager.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UsersController implements UsersApiContractIntrf {

    private final UserService userService;

    @Override
    public ResponseEntity<UserInfoDto> register(UserRegistrationDto request) {
        var user = userService.register(request);
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<JwtResponse> auth(UserCredentialsDto request) {
        var token = userService.auth(request);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Override
    public ResponseEntity<UserInfoDto> getUserById(Long userId) {
        var user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}