package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationEventPayloadRepository extends JpaRepository<NotificationEventPayloadEntity, Long>,
        JpaSpecificationExecutor<NotificationEventPayloadEntity> {

}