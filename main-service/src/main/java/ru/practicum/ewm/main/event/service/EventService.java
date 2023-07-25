package ru.practicum.ewm.main.event.service;

import ru.practicum.ewm.main.event.common.EventSortType;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.event.dto.*;
import ru.practicum.ewm.main.event.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface EventService {
    List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortType sort, Integer from, Integer size);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto getEvent(Long id);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto patchUserEvent(Long userId, Long eventId, UpdateEventUserRequestDto newEventDto);

    EventFullDto patchAdminEvent(Long eventId, UpdateEventAdminRequestDto newEventDto);

    Map<Long, Long> getConfirmedRequestCounts(List<Event> events);

    EventFullDto getUserEventById(Long userId, Long eventId, Integer from, Integer size);
}
