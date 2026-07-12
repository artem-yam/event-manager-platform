package dev.sorokin.eventmanager.security;

import dev.sorokin.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByLogin(username).orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден"));
        return User.withUsername(username)
                .password(user.getPasswordHash())
                .authorities(user.getRole())
                .build();
    }
}
