package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.mapper.RegistrationMapper;
import dev.sorokin.eventmanager.repository.RegistrationRepository;
import dev.sorokin.eventmanager.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.sorokin.eventcommon.validation.ValidationMessages.REGISTRATION_ABSENT_FOR_EVENT_MESSAGE;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ValidationService validationService;
    private final EventService eventService;
    private final UserService userService;
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final EventMapper eventMapper;

    @Transactional
    public void register(Long eventId) {
        var eventEntity = eventService.getEntityById(eventId);

        var activeUser = userService.getActiveUser();

        validationService.validateRegistrationOnEvent(eventEntity, activeUser);

        var newRegistration = registrationMapper.createNewRegistration(eventEntity, activeUser);
        registrationRepository.save(newRegistration);
    }

    @Transactional
    public void cancel(Long eventId) {
        var activeUser = userService.getActiveUser();
        var event = eventService.getEntityById(eventId);

        validationService.validateRegistrationCancel(event, activeUser);

        var result = registrationRepository.deleteByUserAndEvent(activeUser, event);
        if (result == 0) {
            throw new IllegalArgumentException(REGISTRATION_ABSENT_FOR_EVENT_MESSAGE.toString());
        }
    }

    @Transactional
    public List<EventDto> getAllForActiveUser() {
        var activeUser = userService.getActiveUser();

        validationService.validateRegistrationUser(activeUser);
        
        var userReg = registrationRepository.findAllByUser(activeUser);
        var userEvents = userReg.stream().map(RegistrationEntity::getEvent).toList();
        return eventMapper.toDtoList(userEvents);
    }
}
