package ru.practicum.ewm.stat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stat.dto.HitDto;
import ru.practicum.ewm.stat.dto.StatsDto;
import ru.practicum.ewm.stat.service.StatService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto addHit(@RequestBody HitDto hitDto) {
        return service.addHit(hitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDto>> getStats(@RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                   @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                   @RequestParam(required = false) List<String> uris,
                                                   @RequestParam(defaultValue = "false") Boolean unique) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new RuntimeException(String.format("Wrong interval: rangeStart = %s, rangeEnd = %s", start, end));
        }

        List<StatsDto> result = service.getStats(start, end, uris, unique);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
