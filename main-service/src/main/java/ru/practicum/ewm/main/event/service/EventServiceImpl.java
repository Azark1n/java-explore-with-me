package ru.practicum.ewm.main.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.category.entity.Category;
import ru.practicum.ewm.main.category.repository.CategoryRepository;
import ru.practicum.ewm.main.event.common.EventSortType;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.event.common.ParticipationRequestState;
import ru.practicum.ewm.main.event.dto.*;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.event.mapper.EventMapper;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.event.repository.ParticipationRequestRepository;
import ru.practicum.ewm.main.exception.BadRequestException;
import ru.practicum.ewm.main.exception.ForbiddenException;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.StatsInternalException;
import ru.practicum.ewm.main.user.entity.User;
import ru.practicum.ewm.main.user.repository.UserRepository;
import ru.practicum.ewm.stat.client.StatsClient;
import ru.practicum.ewm.stat.dto.StatsDto;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository prRepository;
    private final EventMapper mapper;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;

    @Value(value = "${view.stat.url}")
    private String viewStatUrl;

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size) {
        log.info("Get events by param: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, from={}, size={}, sort={}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size, sort);

        checkStartIsBeforeEnd(rangeStart, rangeEnd);

        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Event> result = eventRepository.findAll(((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (text != null && !text.isBlank()) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
            }

            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").in(categories));
            }

            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
            }

            if (rangeStart == null && rangeEnd == null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now()));
            } else {
                if (rangeStart != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
                }

                if (rangeEnd != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
                }
            }

            predicates.add(criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        }), pageRequest);

        return result.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        log.info("Get user events: userId={}, from={}, size={}", userId, from, size);

        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Event> result = eventRepository.findByInitiator_Id(userId, pageRequest);

        return result.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Get admin events by param: users={}, states = {}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);

        checkStartIsBeforeEnd(rangeStart, rangeEnd);

        PageRequest pageRequest = PageRequest.of(from / size, size);
        Page<Event> result = eventRepository.findAll(((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null && !users.isEmpty()) {
                predicates.add(root.get("initiator").in(users));
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(root.get("state").in(states));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").in(categories));
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        }), pageRequest);

        Map<Long, Long> confirmedRequests = getConfirmedRequestCounts(result.toList());
        Map<Long, Long> viewCounts = getViewCounts(result.toList());

        return result.stream()
                .map(event -> mapper.toFullDto(event, confirmedRequests.getOrDefault(event.getId(), 0L), viewCounts.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long id) {
        log.info("Get event by id = {}", id);

        Event result = eventRepository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", id)));

        Long confirmedRequestsCount = getConfirmedRequestCounts(List.of(result)).get(result.getId());
        Long viewsCount = getViewCounts(List.of(result)).get(result.getId());

        return mapper.toFullDto(result, confirmedRequestsCount, viewsCount);
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException(String.format("Category not found. catId = %d", newEventDto.getCategory())));

        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));

        checkNewEventDate(newEventDto.getEventDate(), LocalDateTime.now().plusHours(2));

        EventState state = EventState.PENDING;

        log.info("Add event. userid = {}, state = {}, {}", userId, state, newEventDto);

        Event saved = eventRepository.save(mapper.toEntity(newEventDto, category, LocalDateTime.now(), initiator, state));

        Long confirmedRequestsCount = getConfirmedRequestCounts(List.of(saved)).getOrDefault(saved.getId(), 0L);
        Long viewsCount = getViewCounts(List.of(saved)).getOrDefault(saved.getId(), 0L);

        return mapper.toFullDto(saved, confirmedRequestsCount, viewsCount);
    }

    @Override
    public EventFullDto patchUserEvent(Long userId, Long eventId, UpdateEventUserRequestDto patchDto) {
        log.info("Patch event. userid = {}, eventId = {}, {}", userId, eventId, patchDto);

        checkNewEventDate(patchDto.getEventDate(), LocalDateTime.now().plusHours(1));

        Event event = eventRepository.findByInitiator_IdAndId(userId, eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. userId = %d, eventId = %d", userId, eventId)));

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(String.format("Forbidden to change state in event state: %s", event.getState()));
        }

        if (patchDto.getCategory() != null) {
            Category category = categoryRepository.findById(patchDto.getCategory()).orElseThrow(
                    () -> new NotFoundException(String.format("Category not found. catId = %d", patchDto.getCategory())));
            event.setCategory(category);
        }

        if (patchDto.getStateAction() != null) {
            switch (patchDto.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        Event saved = eventRepository.save(mapper.partialUpdate(patchDto, event));

        Long confirmedRequestsCount = getConfirmedRequestCounts(List.of(saved)).getOrDefault(saved.getId(), 0L);
        Long viewsCount = getViewCounts(List.of(saved)).getOrDefault(saved.getId(), 0L);

        return mapper.toFullDto(saved, confirmedRequestsCount, viewsCount);
    }

    @Override
    public EventFullDto patchAdminEvent(Long eventId, UpdateEventAdminRequestDto patchDto) {
        log.info("Patch event by admin. eventId = {}, {}", eventId, patchDto);

        checkNewEventDate(patchDto.getEventDate(), LocalDateTime.now().plusHours(1));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException(String.format("Forbidden to change state in event state: %s", event.getState()));
        }

        if (patchDto.getCategory() != null) {
            Category category = categoryRepository.findById(patchDto.getCategory()).orElseThrow(
                    () -> new NotFoundException(String.format("Category not found. catId = %d", patchDto.getCategory())));
            event.setCategory(category);
        }

        if (patchDto.getStateAction() != null) {
            switch (patchDto.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.REJECTED);
                    break;
            }
        }

        Event saved = eventRepository.save(mapper.partialUpdate(patchDto, event));

        Long confirmedRequestsCount = getConfirmedRequestCounts(List.of(saved)).getOrDefault(saved.getId(), 0L);
        Long viewsCount = getViewCounts(List.of(saved)).getOrDefault(saved.getId(), 0L);

        return mapper.toFullDto(saved, confirmedRequestsCount, viewsCount);
    }

    private void checkStartIsBeforeEnd(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException(String.format("Wrong interval: rangeStart = %s, rangeEnd = %s", rangeStart, rangeEnd));
        }
    }

    private void checkNewEventDate(LocalDateTime newEventDate, LocalDateTime minTimeBeforeEventStart) {
        if (newEventDate != null && newEventDate.isBefore(minTimeBeforeEventStart)) {
            throw new BadRequestException(String.format("eventDate should greater than: %s", newEventDate));
        }
    }

    @Override
    public Map<Long, Long> getConfirmedRequestCounts(List<Event> events) {
        Map<Long, Long> confirmedRequests = new HashMap<>();
        prRepository.getCountRequestsEqualStatusAndEventIn(ParticipationRequestState.CONFIRMED, events)
                .forEach(element -> confirmedRequests.put((Long) element[0], (Long) element[1]));
        return confirmedRequests;
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId, Integer from, Integer size) {
        log.info("Get user event. userId = {}, eventId = {}", userId, eventId);

        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));

        Event result = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        Long confirmedRequestsCount = getConfirmedRequestCounts(List.of(result)).get(result.getId());
        Long viewsCount = getViewCounts(List.of(result)).get(result.getId());

        return mapper.toFullDto(result, confirmedRequestsCount, viewsCount);
    }

    public Map<Long, Long> getViewCounts(List<Event> events) {
        Map<Long, Long> views = new HashMap<>();

        Optional<LocalDateTime> minPublishedOn = events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        if (minPublishedOn.isPresent()) {
            LocalDateTime start = minPublishedOn.get();
            LocalDateTime end = LocalDateTime.now();
            List<String> uris = events.stream()
                    .map(Event::getId)
                    .map(id -> (viewStatUrl + id))
                    .collect(Collectors.toList());

            ResponseEntity<Object> response = statsClient.getStats(start, end, uris, true);
            if (!HttpStatus.OK.equals(response.getStatusCode())) {
                throw new StatsInternalException(String.format("Bad response status from stats-server. %s", response));
            }

            List<StatsDto> stats;
            try {
                stats = Arrays.asList(objectMapper.readValue(objectMapper.writeValueAsString(response.getBody()), StatsDto[].class));
            } catch (IOException exception) {
                throw new StatsInternalException(String.format("Bad response body from stats-server. %s", exception.getMessage()));
            }

            stats.forEach(stat -> {
                Long eventId = Long.parseLong(stat.getUri().split("/", 0)[2]);
                views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
            });
        }
        return views;
    }
}
