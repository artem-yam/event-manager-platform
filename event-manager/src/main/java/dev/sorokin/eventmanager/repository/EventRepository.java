package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    List<EventEntity> findAllByOwner(UserEntity owner);

    @Modifying
    @Transactional
    @Query("""
            UPDATE EventEntity e SET e.status = 'STARTED'
            WHERE e.status = 'WAIT_START' AND e.startAt < CURRENT_TIMESTAMP
            """)
    int updateStartedEvents();

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE event
            SET status = 'FINISHED'
            WHERE status = 'STARTED'
              AND start_at + duration_minutes * interval '1 minute' < now()
            """, nativeQuery = true)
    int updateFinishedEvents();
}