package ru.practicum.ewm.main.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Private: События", description = "Закрытый API для работы с событиями")
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@RestController
public class PrivateEventController {
    private final EventService service;

    @Operation(summary = "Добавление нового события")
    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        EventFullDto result = service.addEvent(userId, newEventDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Получение полной информации о событии добавленном текущим пользователем")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable Long userId, @PathVariable Long eventId,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {
        EventFullDto result = service.getUserEventById(userId, eventId, from, size);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Получение событий, добавленных текущим пользователем")
    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@PathVariable Long userId,
                                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventShortDto> result = service.getUserEvents(userId, from, size);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Изменение события добавленного текущим пользователем")
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable Long userId, @PathVariable Long eventId, @RequestBody @Valid UpdateEventUserRequestDto patchDto) {
        EventFullDto result = service.patchUserEvent(userId, eventId, patchDto);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
