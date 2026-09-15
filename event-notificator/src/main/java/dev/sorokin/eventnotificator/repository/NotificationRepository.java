package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEntity;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, JpaSpecificationExecutor<NotificationEntity> {

    @EntityGraph(attributePaths = "payload", type = EntityGraph.EntityGraphType.LOAD)
    List<NotificationEntity> getAllByUserIdAndIsReadFalse(Long userId);

    @Modifying
    @Query("UPDATE NotificationEntity n " +
            "SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP " +
            "WHERE n.userId = :userId AND n.isRead is false AND n.id IN :notificationIds")
    int markAsRead(Long userId, List<Long> notificationIds);

}