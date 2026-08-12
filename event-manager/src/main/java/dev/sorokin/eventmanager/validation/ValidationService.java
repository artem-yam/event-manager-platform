package dev.sorokin.eventmanager.validation;

import dev.sorokin.eventmanager.dto.*;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@NoArgsConstructor
@Setter
public class ValidationService {

    @Value("${validation.messages.location.empty_location_name}")
    private String LOCATION_NAME_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.location.empty_location_address}")
    private String LOCATION_ADDRESS_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.location.empty_location_capacity}")
    private String LOCATION_CAPACITY_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.location.location_capacity_not_enough_for_events}")
    private String LOCATION_CAPACITY_NOT_ENOUGH_FOR_EVENTS_VALIDATION_ERROR_MESSAGE;

    @Value("${validation.messages.user.empty_user_login}")
    private String USER_LOGIN_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.user.empty_user_password}")
    private String USER_PASSWORD_VALIDATION_ERROR_MESSAGE;
    @Value("${validation.messages.user.empty_user_age}")
    private String USER_AGE_VALIDATION_ERROR_MESSAGE;

    @Value("${validation.messages.event.exceed_location_capacity}")
    private String EVENT_PLACES_EXCEED_LOCATION_CAPACITY_ERROR_MESSAGE;
    @Value("${validation.messages.event.date_time_overlaps}")
    private String EVENT_DATE_TIME_OVERLAPS_ERROR_MESSAGE;
    @Value("${validation.messages.event.wrong_user}")
    private String EVENT_WRONG_USER_ERROR_MESSAGE;
    @Value("${validation.messages.event.wrong_status}")
    private String EVENT_WRONG_STATUS_ERROR_MESSAGE;
    @Value("${validation.messages.event.wrong_new_max_places_on_update}")
    private String EVENT_WRONG_NEW_MAX_PLACES_ON_UPDATE_ERROR_MESSAGE;

    @Value("${validation.messages.registration.admin_registration}")
    private String REGISTRATION_FOR_ADMIN_ERROR_MESSAGE;
    @Value("${validation.messages.registration.event_no_places}")
    private String REGISTRATION_EVENT_NO_PLACES_ERROR_MESSAGE;
    @Value("${validation.messages.registration.user_already_registered}")
    private String REGISTRATION_USER_ALREADY_REGISTERED_ERROR_MESSAGE;

    public void validateLocation(LocationDto updatedLocationDto) {
        validateLocation(0, updatedLocationDto);
    }

    public void validateLocation(int currentEventsMaxPlaces, LocationDto updatedLocationDto) {
        if (updatedLocationDto.getName() == null) {
            throw new IllegalArgumentException(LOCATION_NAME_VALIDATION_ERROR_MESSAGE);
        }
        if (updatedLocationDto.getAddress() == null) {
            throw new IllegalArgumentException(LOCATION_ADDRESS_VALIDATION_ERROR_MESSAGE);
        }
        if (updatedLocationDto.getCapacity() == null || updatedLocationDto.getCapacity() < 5) {
            throw new IllegalArgumentException(LOCATION_CAPACITY_VALIDATION_ERROR_MESSAGE);
        }
        if (currentEventsMaxPlaces > updatedLocationDto.getCapacity()) {
            throw new IllegalArgumentException(LOCATION_CAPACITY_NOT_ENOUGH_FOR_EVENTS_VALIDATION_ERROR_MESSAGE);
        }
    }

    public void validateUser(UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto.getLogin() == null) {
            throw new IllegalArgumentException(USER_LOGIN_VALIDATION_ERROR_MESSAGE);
        }
        if (userRegistrationDto.getPassword() == null) {
            throw new IllegalArgumentException(USER_PASSWORD_VALIDATION_ERROR_MESSAGE);
        }
        if (userRegistrationDto.getAge() == null || userRegistrationDto.getAge() < 18) {
            throw new IllegalArgumentException(USER_AGE_VALIDATION_ERROR_MESSAGE);
        }
    }

    public void validateEventOnCreate(EventRequestDto eventDto, int locationCapacity,
                                      List<EventEntity> locationOtherEvents) {
        baseValidateEvent(null, eventDto, locationCapacity, locationOtherEvents);
    }

    private void baseValidateEvent(Long eventId, EventRequestDto eventDto, int locationCapacity,
                                   List<EventEntity> locationOtherEvents) {
        if (eventDto.getMaxPlaces() > locationCapacity) {
            throw new IllegalArgumentException(EVENT_PLACES_EXCEED_LOCATION_CAPACITY_ERROR_MESSAGE);
        }
        if (eventsDateTimeOverlaps(eventId, eventDto.getDate(), eventDto.getDuration(), locationOtherEvents)) {
            throw new IllegalArgumentException(EVENT_DATE_TIME_OVERLAPS_ERROR_MESSAGE);
        }
    }

    public void validateEventOnUpdate(EventEntity actualEvent, EventRequestDto eventDto,
                                      LocationEntity eventLocation, UserEntity expectedOwner) {
        validateOwner(actualEvent.getOwner(), expectedOwner);
        validateStatus(actualEvent);

        if (actualEvent.getOccupiedPlaces() > eventDto.getMaxPlaces()) {
            throw new IllegalArgumentException(EVENT_WRONG_NEW_MAX_PLACES_ON_UPDATE_ERROR_MESSAGE);
        }

        baseValidateEvent(actualEvent.getId(), eventDto, eventLocation.getCapacity(), eventLocation.getEvents());
    }

    public void validateEventOnCancel(EventEntity actualEvent, UserEntity expectedOwner) {
        validateOwner(actualEvent.getOwner(), expectedOwner);
        validateStatus(actualEvent);
    }

    public void validateRegistrationOnEvent(EventEntity event, UserEntity user) {
        validateRegistrationUser(user);
        validateStatus(event);

        if (event.getRegistrations().stream().anyMatch(otherReg -> otherReg.getUser().equals(user))) {
            throw new IllegalArgumentException(REGISTRATION_USER_ALREADY_REGISTERED_ERROR_MESSAGE);
        }

        if (event.getOccupiedPlaces().equals(event.getMaxPlaces())) {
            throw new IllegalArgumentException(REGISTRATION_EVENT_NO_PLACES_ERROR_MESSAGE);
        }
    }

    public void validateRegistrationCancel(EventEntity event, UserEntity user) {
        validateRegistrationUser(user);
        validateStatus(event);
    }

    private void validateOwner(UserEntity owner, UserEntity expectedOwner) {
        if (!owner.getId().equals(expectedOwner.getId())
                && !UserRole.ADMIN.toString().equals(expectedOwner.getRole())) {
            throw new IllegalArgumentException(EVENT_WRONG_USER_ERROR_MESSAGE);
        }
    }

    public void validateRegistrationUser(UserEntity user) {
        if (!UserRole.USER.toString().equals(user.getRole())) {
            throw new IllegalArgumentException(REGISTRATION_FOR_ADMIN_ERROR_MESSAGE);
        }
    }

    private void validateStatus(EventEntity event) {
        if (!EventStatus.WAIT_START.toString().equals(event.getStatus())) {
            throw new IllegalArgumentException(EVENT_WRONG_STATUS_ERROR_MESSAGE);
        }
    }

    private boolean eventsDateTimeOverlaps(Long eventId, LocalDateTime start, int durationMinutes,
                                           List<EventEntity> locationEvents) {
        var end = start.plusMinutes(durationMinutes);

        for (EventEntity eventToCheck : locationEvents) {
            if (!eventToCheck.getId().equals(eventId)) {
                var otherStart = eventToCheck.getStartAt();
                var otherEnd = otherStart.plusMinutes(eventToCheck.getDurationMinutes());

                if (start.isBefore(otherEnd) && otherStart.isBefore(end)) {
                    return true;
                }
            }
        }
        return false;
    }
}
