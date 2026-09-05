package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, JpaSpecificationExecutor<NotificationEntity> {

    @EntityGraph(attributePaths = "payload", type = EntityGraph.EntityGraphType.LOAD)
    List<NotificationEntity> getAllByUserIdAndIsReadFalse(Long userId);

}