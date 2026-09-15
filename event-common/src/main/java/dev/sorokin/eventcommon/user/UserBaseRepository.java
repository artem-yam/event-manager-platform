package dev.sorokin.eventcommon.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBaseRepository<T extends UserBaseEntity> extends JpaRepository<T, Long> {

    boolean existsByLogin(String login);

    Optional<T> findByLogin(String login);

}