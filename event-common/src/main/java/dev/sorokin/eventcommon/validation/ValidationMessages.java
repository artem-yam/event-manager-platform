package dev.sorokin.eventcommon.validation;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ValidationMessages {
    LOCATION_NAME_VALIDATION_ERROR_MESSAGE("location.empty_location_name"),
    LOCATION_ADDRESS_VALIDATION_ERROR_MESSAGE("location.empty_location_address"),
    LOCATION_CAPACITY_VALIDATION_ERROR_MESSAGE("location.empty_location_capacity"),
    LOCATION_CAPACITY_NOT_ENOUGH_FOR_EVENTS_VALIDATION_ERROR_MESSAGE("location.location_capacity_not_enough_for_events"),
    USER_LOGIN_VALIDATION_ERROR_MESSAGE("user.empty_user_login"),
    USER_PASSWORD_VALIDATION_ERROR_MESSAGE("user.empty_user_password"),
    USER_AGE_VALIDATION_ERROR_MESSAGE("user.empty_user_age"),
    EVENT_PLACES_EXCEED_LOCATION_CAPACITY_ERROR_MESSAGE("event.exceed_location_capacity"),
    EVENT_DATE_TIME_OVERLAPS_ERROR_MESSAGE("event.date_time_overlaps"),
    EVENT_WRONG_USER_ERROR_MESSAGE("event.wrong_user"),
    EVENT_WRONG_STATUS_ERROR_MESSAGE("event.wrong_status"),
    EVENT_WRONG_NEW_MAX_PLACES_ON_UPDATE_ERROR_MESSAGE("event.wrong_new_max_places_on_update"),
    REGISTRATION_FOR_ADMIN_ERROR_MESSAGE("registration.admin_registration"),
    REGISTRATION_EVENT_NO_PLACES_ERROR_MESSAGE("registration.event_no_places"),
    REGISTRATION_USER_ALREADY_REGISTERED_ERROR_MESSAGE("registration.user_already_registered"),
    LOCATION_NOT_FOUND_MESSAGE("location.not_found"),
    LOCATION_WITH_EVENTS_MESSAGE("location.with_events"),
    USER_BY_ID_NOT_FOUND_MESSAGE("user.not_found_by_id"),
    USER_BY_LOGIN_NOT_FOUND_MESSAGE("user.not_found_by_login"),
    LOGIN_TAKEN_MESSAGE("user.login_taken"),
    REGISTRATION_ABSENT_FOR_EVENT_MESSAGE("registration.absent_for_event");

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages", Locale.getDefault());

    private final String msgPath;

    ValidationMessages(String msgPath) {
        this.msgPath = msgPath;
    }

    @Override
    public String toString() {
        return RESOURCE_BUNDLE.getString(msgPath);
    }
}
