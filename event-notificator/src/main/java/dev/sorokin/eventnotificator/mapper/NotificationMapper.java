package dev.sorokin.eventnotificator.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.enums.EventType;
import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.dto.NotificationPayload;
import dev.sorokin.eventnotificator.dto.NotificationResponse;
import dev.sorokin.eventnotificator.entity.NotificationEntity;
import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR,
        componentModel = "spring",
        imports = {LocalDateTime.class, ObjectMapper.class})
@Component
@Setter
@NoArgsConstructor
public abstract class NotificationMapper {

    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(target = "payloadJson", source = "msg")
    @Mapping(target = "id", ignore = true)
    public abstract NotificationEventPayloadEntity createPayloadEntity(EventChangeKafkaMessage msg);

    public List<NotificationEntity> createEntities(NotificationEventPayloadEntity payloadEntity, List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<NotificationEntity> result = new ArrayList<>();
        userIds.forEach(subId -> {
            result.add(createEntity(payloadEntity, subId));
        });
        return result;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "readAt", ignore = true)
    @Mapping(target = "read", ignore = true)
    @Mapping(target = "payload", source = "payloadEntity")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    public abstract NotificationEntity createEntity(NotificationEventPayloadEntity payloadEntity, Long userId);

    public String createPayloadJson(EventChangeKafkaMessage msg) {
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось записать текст сообщения нотификации", e);
        }
    }

    public abstract List<NotificationResponse> toNotificationResponseList(List<NotificationEntity> entity);

    @Mapping(target = "notificationId", source = "id")
    @Mapping(target = "type", source = "payload.eventType")
    @Mapping(target = "eventId", source = "payload.eventId")
    @Mapping(target = "isRead", source = "read")
    @Mapping(target = "message", source = "payload.eventType", qualifiedByName = "getEventTypeMsg")
    public abstract NotificationResponse toNotificationResponse(NotificationEntity entity);

    @Named("getEventTypeMsg")
    public String getEventTypeMsg(EventType eventType) {
        return eventType.getDescription();
    }

    public NotificationPayload parsePayloadJson(NotificationEventPayloadEntity entity) {
        try {
            return objectMapper.readValue(entity.getPayloadJson(), NotificationPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Не удалось распарсить текст сообщения нотификации", e);
        }
    }

}
