package ru.practicum.ewm.main.event.service;

import ru.practicum.ewm.main.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.event.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getEventParticipationRequests(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipationRequestsByUserId(Long userId);

    EventRequestStatusUpdateResult setStatusForGroup(Long userId, Long eventId, EventRequestStatusUpdateRequest statusDto);
}
