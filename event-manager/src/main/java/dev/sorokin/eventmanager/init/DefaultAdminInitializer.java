package dev.sorokin.eventmanager.init;

import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        if (userRepository.existsByLogin("admin")) {
            return;
        }
        userRepository.save(new UserEntity("admin", 30, encoder.encode("admin"), "ADMIN"));
    }
}
