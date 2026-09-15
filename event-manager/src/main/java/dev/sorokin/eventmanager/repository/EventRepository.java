package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    List<EventEntity> findAllByOwner(UserEntity owner);

    @Query("""
            SELECT e FROM EventEntity e
            WHERE e.status = 'WAIT_START' AND e.startAt < CURRENT_TIMESTAMP
            """)
    @EntityGraph(attributePaths = {"registrations"}, type= EntityGraph.EntityGraphType.LOAD)
    List<EventEntity> getEventsToStart();

    @Query(value = """
            SELECT e FROM EventEntity e
            LEFT JOIN FETCH e.registrations r
            WHERE e.status = 'STARTED'
              AND TIMESTAMPADD(MINUTE, e.durationMinutes, e.startAt) < CURRENT_TIMESTAMP
            """)
    List<EventEntity> getEventsToFinish();
}