package ru.practicum.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.CommonUtils;
import ru.practicum.ewm.main.event.common.EventSortType;
import ru.practicum.ewm.main.event.dto.EventFullDto;
import ru.practicum.ewm.main.event.dto.EventShortDto;
import ru.practicum.ewm.main.event.service.EventService;
import ru.practicum.ewm.stat.client.StatsClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class PublicEventController {
    private final EventService service;
    private final StatsClient statsClient;

    @Value(value = "${app.name}")
    private String appName;

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable @NotNull Long id, HttpServletRequest request) {
        EventFullDto result = service.getEvent(id);

        statsClient.addHit(appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@RequestParam(required = false) String text,
                                                         @RequestParam(required = false) List<Integer> categories,
                                                         @RequestParam(required = false) Boolean paid,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = CommonUtils.DATE_FORMAT) LocalDateTime rangeStart,
                                                         @RequestParam(required = false) @DateTimeFormat(pattern = CommonUtils.DATE_FORMAT) LocalDateTime rangeEnd,
                                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                         @RequestParam(required = false) EventSortType sort,
                                                         @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                         HttpServletRequest request) {

        List<EventShortDto> result = service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        statsClient.addHit(appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
