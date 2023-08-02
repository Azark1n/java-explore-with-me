package ru.practicum.ewm.main.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.CommonUtils;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.event.dto.EventFullDto;
import ru.practicum.ewm.main.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.ewm.main.event.service.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Admin: События", description = "API для работы с событиями")
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@RestController
public class AdminEventController {
    private final EventService service;

    @Operation(summary = "Поиск событий")
    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(@RequestParam(required = false) List<Long> users,
                                                         @RequestParam(required = false) List<EventState> states,
                                                         @RequestParam(required = false) List<Integer> categories,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = CommonUtils.DATE_FORMAT) LocalDateTime rangeStart,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = CommonUtils.DATE_FORMAT) LocalDateTime rangeEnd,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size) {

        List<EventFullDto> result = service.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Редактирование данных события и его статуса (отклонение/публикация)")
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchEvent(@PathVariable Long eventId, @RequestBody @Valid UpdateEventAdminRequestDto patchDto) {
        EventFullDto result = service.patchAdminEvent(eventId, patchDto);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
