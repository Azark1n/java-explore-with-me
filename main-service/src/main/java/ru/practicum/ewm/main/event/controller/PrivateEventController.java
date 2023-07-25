package ru.practicum.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.event.dto.EventFullDto;
import ru.practicum.ewm.main.event.dto.EventShortDto;
import ru.practicum.ewm.main.event.dto.NewEventDto;
import ru.practicum.ewm.main.event.dto.UpdateEventUserRequestDto;
import ru.practicum.ewm.main.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@RestController
public class PrivateEventController {
    private final EventService service;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@PathVariable Long userId,
                                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventShortDto> result = service.getUserEvents(userId, from, size);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long userId, @PathVariable Long eventId,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        EventFullDto result = service.getUserEventById(userId, eventId, from, size);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        EventFullDto result = service.addEvent(userId, newEventDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid UpdateEventUserRequestDto patchDto) {
        EventFullDto result = service.patchUserEvent(userId, eventId, patchDto);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
