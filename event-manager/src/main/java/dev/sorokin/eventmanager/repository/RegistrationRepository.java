package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    @Modifying
    int deleteByUserAndEvent(UserEntity user, EventEntity event);

    @EntityGraph(attributePaths = "event")
    List<RegistrationEntity> findAllByUser(UserEntity user);
}