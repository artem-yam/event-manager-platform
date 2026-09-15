package dev.sorokin.eventmanager.validation;

import dev.sorokin.eventmanager.dto.*;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static dev.sorokin.eventcommon.validation.ValidationMessages.*;

@Component
@NoArgsConstructor
@Setter
public class ValidationService {

    public void validateLocation(LocationDto updatedLocationDto) {
        validateLocation(0, updatedLocationDto);
    }

    public void validateLocation(int currentEventsMaxPlaces, LocationDto updatedLocationDto) {
        if (updatedLocationDto.getName() == null) {
            throw new IllegalArgumentException(LOCATION_NAME_VALIDATION_ERROR_MESSAGE.toString());
        }
        if (updatedLocationDto.getAddress() == null) {
            throw new IllegalArgumentException(LOCATION_ADDRESS_VALIDATION_ERROR_MESSAGE.toString());
        }
        if (updatedLocationDto.getCapacity() == null || updatedLocationDto.getCapacity() < 5) {
            throw new IllegalArgumentException(LOCATION_CAPACITY_VALIDATION_ERROR_MESSAGE.toString());
        }
        if (currentEventsMaxPlaces > updatedLocationDto.getCapacity()) {
            throw new IllegalArgumentException(LOCATION_CAPACITY_NOT_ENOUGH_FOR_EVENTS_VALIDATION_ERROR_MESSAGE.toString());
        }
    }

    public void validateUser(UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto.getLogin() == null) {
            throw new IllegalArgumentException(USER_LOGIN_VALIDATION_ERROR_MESSAGE.toString());
        }
        if (userRegistrationDto.getPassword() == null) {
            throw new IllegalArgumentException(USER_PASSWORD_VALIDATION_ERROR_MESSAGE.toString());
        }
        if (userRegistrationDto.getAge() == null || userRegistrationDto.getAge() < 18) {
            throw new IllegalArgumentException(USER_AGE_VALIDATION_ERROR_MESSAGE.toString());
        }
    }

    public void validateEventOnCreate(EventRequestDto eventDto, int locationCapacity,
                                      List<EventEntity> locationOtherEvents) {
        baseValidateEvent(null, eventDto, locationCapacity, locationOtherEvents);
    }

    private void baseValidateEvent(Long eventId, EventRequestDto eventDto, int locationCapacity,
                                   List<EventEntity> locationOtherEvents) {
        if (eventDto.getMaxPlaces() > locationCapacity) {
            throw new IllegalArgumentException(EVENT_PLACES_EXCEED_LOCATION_CAPACITY_ERROR_MESSAGE.toString());
        }
        if (eventsDateTimeOverlaps(eventId, eventDto.getDate(), eventDto.getDuration(), locationOtherEvents)) {
            throw new IllegalArgumentException(EVENT_DATE_TIME_OVERLAPS_ERROR_MESSAGE.toString());
        }
    }

    public void validateEventOnUpdate(EventEntity actualEvent, EventRequestDto eventDto,
                                      LocationEntity eventLocation, UserEntity expectedOwner) {
        validateOwner(actualEvent.getOwner(), expectedOwner);
        validateStatus(actualEvent);

        if (actualEvent.getOccupiedPlaces() > eventDto.getMaxPlaces()) {
            throw new IllegalArgumentException(EVENT_WRONG_NEW_MAX_PLACES_ON_UPDATE_ERROR_MESSAGE.toString());
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
            throw new IllegalArgumentException(REGISTRATION_USER_ALREADY_REGISTERED_ERROR_MESSAGE.toString());
        }

        if (event.getOccupiedPlaces().equals(event.getMaxPlaces())) {
            throw new IllegalArgumentException(REGISTRATION_EVENT_NO_PLACES_ERROR_MESSAGE.toString());
        }
    }

    public void validateRegistrationCancel(EventEntity event, UserEntity user) {
        validateRegistrationUser(user);
        validateStatus(event);
    }

    private void validateOwner(UserEntity owner, UserEntity expectedOwner) {
        if (!owner.getId().equals(expectedOwner.getId())
                && !UserRole.ADMIN.toString().equals(expectedOwner.getRole())) {
            throw new IllegalArgumentException(EVENT_WRONG_USER_ERROR_MESSAGE.toString());
        }
    }

    public void validateRegistrationUser(UserEntity user) {
        if (!UserRole.USER.toString().equals(user.getRole())) {
            throw new IllegalArgumentException(REGISTRATION_FOR_ADMIN_ERROR_MESSAGE.toString());
        }
    }

    private void validateStatus(EventEntity event) {
        if (!EventStatus.WAIT_START.toString().equals(event.getStatus())) {
            throw new IllegalArgumentException(EVENT_WRONG_STATUS_ERROR_MESSAGE.toString());
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
