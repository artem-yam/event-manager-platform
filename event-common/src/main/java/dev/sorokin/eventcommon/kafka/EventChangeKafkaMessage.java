package dev.sorokin.eventcommon.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventChangeKafkaMessage {

    private UUID messageId;

    private String eventType;

    private Long eventId;

    private String eventName;

    private LocalDateTime occurredAt;

    private Long ownerId;

    private Long changedById;

    private List<Long> subscribers;

    private  List<ChangeItem> changes;
}