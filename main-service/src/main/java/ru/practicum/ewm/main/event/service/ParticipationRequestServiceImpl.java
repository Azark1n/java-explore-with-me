package ru.practicum.ewm.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.event.common.ParticipationRequestState;
import ru.practicum.ewm.main.event.common.ParticipationRequestStateAction;
import ru.practicum.ewm.main.event.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.main.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.main.event.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.event.entity.ParticipationRequest;
import ru.practicum.ewm.main.event.mapper.ParticipationRequestMapper;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.event.repository.ParticipationRequestRepository;
import ru.practicum.ewm.main.exception.ForbiddenException;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.user.entity.User;
import ru.practicum.ewm.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final ParticipationRequestMapper mapper;

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        log.info("Add participation request. userId = {}, eventId = {}", userId, eventId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenException("It's your event. You're owner, not participant");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Event isn't published");
        }

        Optional<ParticipationRequest> oldRequest = repository.findByEvent_IdAndRequester_Id(eventId, userId);
        if (oldRequest.isPresent()) {
            throw new ForbiddenException("Already requested");
        }

        Long confirmedRequestsCount = eventService.getConfirmedRequestCounts(List.of(event)).getOrDefault(event.getId(), 0L);
        if (event.getParticipantLimit() != 0 && confirmedRequestsCount >= event.getParticipantLimit()) {
            throw new ForbiddenException("The participant limit has been reached");
        }

        ParticipationRequest newRequest = new ParticipationRequest();
        newRequest.setRequester(user);
        newRequest.setEvent(event);
        newRequest.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            newRequest.setStatus(ParticipationRequestState.CONFIRMED);
        } else {
            newRequest.setStatus(ParticipationRequestState.PENDING);
        }

        return mapper.toDto(repository.save(newRequest));
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        log.info("Cancel participation request. userId = {}, requestId = {}", userId, requestId);

        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));

        ParticipationRequest request = repository.findById(requestId).orElseThrow(
                () -> new NotFoundException(String.format("Participation request not found. requestId = %d", requestId)));

        request.setStatus(ParticipationRequestState.CANCELED);
        return mapper.toDto(repository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipationRequests(Long userId, Long eventId) {
        log.info("Get events participation requests. userId = {}, eventId = {}", userId, eventId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ForbiddenException(String.format("You're not owner event. EventId = %d", eventId));
        }

        List<ParticipationRequest> result = repository.findByEvent(event);
        return result.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsByUserId(Long userId) {
        log.info("Get participation requests. userId = {}", userId);

        List<ParticipationRequest> result = repository.findByRequester_Id(userId);
        return result.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult setStatusForGroup(Long userId, Long eventId, EventRequestStatusUpdateRequest statusDto) {
        log.info("Set status for group participation requests. userId = {}, eventId = {}, {}", userId, eventId, statusDto);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ForbiddenException(String.format("You're not owner event. EventId = %d", eventId));
        }

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return EventRequestStatusUpdateResult.builder().build();
        }

        List<ParticipationRequest> requests = repository.findAllByIdIn(statusDto.getRequestIds());
        if (requests.size() != statusDto.getRequestIds().size()) {
            throw new NotFoundException("Some participation requests were not found");
        }
        if (!requests.stream()
                .map(ParticipationRequest::getStatus)
                .allMatch(ParticipationRequestState.PENDING::equals)) {
            throw new ForbiddenException("Only pending applications can be changed");
        }

        Long confirmedRequestsCount = eventService.getConfirmedRequestCounts(List.of(event)).getOrDefault(event.getId(), 0L);
        if (confirmedRequestsCount >= event.getParticipantLimit()) {
            throw new ForbiddenException("The participant limit has been reached");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        if (ParticipationRequestStateAction.REJECTED.equals(statusDto.getStatus())) {
            for (ParticipationRequest request : requests) {
                request.setStatus(ParticipationRequestState.REJECTED);
                rejectedRequests.add(mapper.toDto(repository.save(request)));
            }
        } else {
            for (ParticipationRequest request : requests) {
                if (confirmedRequestsCount >= event.getParticipantLimit()) {
                    request.setStatus(ParticipationRequestState.REJECTED);
                    rejectedRequests.add(mapper.toDto(repository.save(request)));
                } else {
                    request.setStatus(ParticipationRequestState.CONFIRMED);
                    confirmedRequests.add(mapper.toDto(repository.save(request)));
                    confirmedRequestsCount++;
                }
            }
        }

        return EventRequestStatusUpdateResult.builder().confirmedRequests(confirmedRequests).rejectedRequests(rejectedRequests).build();
    }
}
