package ru.practicum.ewm.main.event.controller;

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

@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@RestController
public class PrivateParticipationRequestController {
    private final ParticipationRequestService service;

    @GetMapping("/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getParticipationRequestsByUserId(@PathVariable Long userId) {
        List<ParticipationRequestDto> result = service.getParticipationRequestsByUserId(userId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/requests")
    public ResponseEntity<ParticipationRequestDto> addParticipationRequest(@PathVariable Long userId, @RequestParam @NotNull @Min(1) Long eventId) {
        ParticipationRequestDto result = service.addParticipationRequest(userId, eventId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelParticipationRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        ParticipationRequestDto result = service.cancelParticipationRequest(userId, requestId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventParticipationRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        List<ParticipationRequestDto> result = service.getEventParticipationRequests(userId, eventId);

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> setStatusForGroup(@PathVariable Long userId, @PathVariable Long eventId,
                                                                            @RequestBody @Valid EventRequestStatusUpdateRequest statusDto) {
        EventRequestStatusUpdateResult result = service.setStatusForGroup(userId, eventId, statusDto);

        return ResponseEntity.ok(result);
    }
}
