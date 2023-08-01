package ru.practicum.ewm.main.event.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.event.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.event.service.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "Private: Запросы на участие", description = "Закрытый API для работы с запросами текущего пользователя на участие в событиях")
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@RestController
public class PrivateParticipationRequestController {
    private final ParticipationRequestService service;

    @Operation(summary = "Добавление запроса от текущего пользователя на участие в событии")
    @PostMapping("/requests")
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(@PathVariable Long userId, @RequestParam @NotNull @Min(1) Long eventId) {
        ParticipationRequestDto result = service.addParticipationRequest(userId, eventId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Отмена своего запроса на участие в событии")
    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelParticipationRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        ParticipationRequestDto result = service.cancelParticipationRequest(userId, requestId);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получение информации о запросах на участие в событии текущего пользователя")
    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipationRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        List<ParticipationRequestDto> result = service.getEventParticipationRequests(userId, eventId);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получение информации о заявках текущего пользователя на участие в чужих событиях")
    @GetMapping("/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequestsByUserId(@PathVariable Long userId) {
        List<ParticipationRequestDto> result = service.getParticipationRequestsByUserId(userId);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя")
    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> setStatusForGroup(@PathVariable Long userId, @PathVariable Long eventId,
                                                                            @RequestBody @Valid EventRequestStatusUpdateRequest statusDto) {
        EventRequestStatusUpdateResult result = service.setStatusForGroup(userId, eventId, statusDto);

        return ResponseEntity.ok(result);
    }
}
