package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.dto.EventRequestDto;
import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.dto.EventStatus;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.repository.EventRepository;
import dev.sorokin.eventmanager.service.notification.NotificationService;
import dev.sorokin.eventmanager.utils.SpecificationUtils;
import dev.sorokin.eventmanager.validation.ValidationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.sorokin.eventmanager.utils.SpecificationUtils.between;
import static dev.sorokin.eventmanager.utils.SpecificationUtils.equal;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final String EVENT_NOT_FOUND_MESSAGE = "Не найдено событие с id %s";

    private final ValidationService validationService;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final LocationService locationService;
    private final NotificationService notificationService;

    public EventDto create(EventRequestDto request) {
        var eventLocation = locationService.getEntityById(request.getLocationId());

        validationService.validateEventOnCreate(request, eventLocation.getCapacity(), eventLocation.getEvents());

        var activeUser = userService.getActiveUser();

        var event = eventMapper.createEntity(request, activeUser, eventLocation);
        event = eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public EventDto getById(Long eventId) {
        var entity = getEntityById(eventId);
        return eventMapper.toDto(entity);
    }

    @Transactional
    public EventDto update(Long eventId, EventRequestDto request) {
        var eventEntity = getEntityById(eventId);
        var eventLocation = locationService.getEntityById(request.getLocationId());

        var activeUser = userService.getActiveUser();

        validationService.validateEventOnUpdate(eventEntity, request, eventLocation, activeUser);

        notificationService.notifyUpdate(eventEntity, request, activeUser.getId());

        eventMapper.updateEntity(eventEntity, request);
        eventRepository.save(eventEntity);

        return eventMapper.toDto(eventEntity);
    }

    @Transactional
    public void delete(Long eventId) {
        var eventEntity = getEntityById(eventId);

        var activeUser = userService.getActiveUser();

        validationService.validateEventOnCancel(eventEntity, activeUser);

        eventEntity.setStatus(EventStatus.CANCELLED.toString());

        notificationService.notifyDelete(eventEntity, activeUser.getId());
    }

    public List<EventDto> search(EventSearchRequestDto request) {
        Specification<EventEntity> spec = SpecificationUtils.<EventEntity>
                        equal(request.getName(), "name")
                .and(between(request.getPlacesMin(), request.getPlacesMax(), "maxPlaces"))
                .and(between(request.getDateStartAfter(), request.getDateStartBefore(), "startAt"))
                .and(between(request.getCostMin(), request.getCostMax(), "cost"))
                .and(between(request.getDurationMin(), request.getDurationMax(), "durationMinutes"))
                .and(equal(request.getLocationId(), "location.id"))
                .and(equal(request.getEventStatus() == null ? null : request.getEventStatus().toString(), "status"));

        var result = eventRepository.findAll(spec);
        return eventMapper.toDtoList(result);
    }

    public List<EventDto> getAllForActiveUser() {
        var activeUser = userService.getActiveUser();

        var events = eventRepository.findAllByOwner(activeUser);
        return eventMapper.toDtoList(events);
    }

    protected EventEntity getEntityById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE.formatted(eventId)));
    }

    @Transactional
    public List<EventEntity> startEvents() {
        var eventsToStart = eventRepository.getEventsToStart();
        eventsToStart.forEach(event -> event.setStatus(EventStatus.STARTED.toString()));
        eventRepository.saveAll(eventsToStart);
        return eventsToStart;
    }

    @Transactional
    public List<EventEntity> finishEvents() {
        var eventsToFinish = eventRepository.getEventsToFinish();
        eventsToFinish.forEach(event -> event.setStatus(EventStatus.FINISHED.toString()));
        eventRepository.saveAll(eventsToFinish);
        return eventsToFinish;
    }
}
