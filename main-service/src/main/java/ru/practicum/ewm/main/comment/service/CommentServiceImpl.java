package ru.practicum.ewm.main.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.main.comment.common.ClaimState;
import ru.practicum.ewm.main.comment.dto.ClaimDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;
import ru.practicum.ewm.main.comment.entity.Claim;
import ru.practicum.ewm.main.comment.entity.Comment;
import ru.practicum.ewm.main.comment.mapper.ClaimMapper;
import ru.practicum.ewm.main.comment.mapper.CommentMapper;
import ru.practicum.ewm.main.comment.repository.ClaimRepository;
import ru.practicum.ewm.main.comment.repository.CommentRepository;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.BadRequestException;
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
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ClaimRepository claimRepository;
    private final CommentMapper mapper;
    private final ClaimMapper claimMapper;

    private static final int ALLOWED_EDITING_TIME_SEC = 600;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        log.info("Add comment. userId = {}, eventId = {}, {}", userId, eventId, newCommentDto);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));
        if (Boolean.TRUE.equals(user.getBanned())) {
            throw new ForbiddenException("You are banned");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new BadRequestException(String.format("Only published events can be commented. eventId = %d", eventId));
        }

        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());

        return mapper.toDto(repository.save(comment));
    }

    @Override
    public CommentDto patchComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        log.info("Patch comment. userId = {}, commentId = {}, {}", userId, commentId, newCommentDto);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));
        if (Boolean.TRUE.equals(user.getBanned())) {
            throw new ForbiddenException("You are banned");
        }

        Comment comment = repository.findById(commentId).orElseThrow(
                () -> new NotFoundException(String.format("Comment not found. commentId = %d", commentId)));
        if (!user.equals(comment.getAuthor())) {
            throw new ForbiddenException("You are not owner");
        }
        if (comment.getDeletedOn() != null) {
            throw new BadRequestException("The comment was deleted");
        }
        if (LocalDateTime.now().isAfter(comment.getCreatedOn().plusSeconds(ALLOWED_EDITING_TIME_SEC))) {
            throw new ForbiddenException("Time to edit is over");
        }
        if (claimRepository.findByComment(comment).isPresent()) {
            throw new ForbiddenException(String.format("Comment with id = %d has one or more claims", commentId));
        }
        Optional<Comment> lastComment = repository.findFirstByEventOrderByCreatedOnDesc(comment.getEvent());
        if (lastComment.isPresent() && !lastComment.get().equals(comment)) {
            throw new ForbiddenException("Only the last comment is allowed to be edited");
        }

        comment.setText(newCommentDto.getText());
        comment.setEditedOn(LocalDateTime.now());

        return mapper.toDto(repository.save(comment));
    }

    @Override
    public List<CommentDto> getComments(Long eventId, Integer from, Integer size) {
        log.info("Get comments. eventId = {}", eventId);

        Pageable pageable = PageRequest.of(from / size, size);

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        return repository.findByEventAndDeletedOnNullOrderByCreatedOnDesc(event, pageable).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentId) {
        log.info("Delete comment. commentId = {}", commentId);

        Comment comment = repository.findById(commentId).orElseThrow(
                () -> new NotFoundException(String.format("Comment not found. commentId = %d", commentId)));
        if (comment.getDeletedOn() != null) {
            throw new BadRequestException(String.format("Comment is already deleted. commentId = %d", commentId));
        }

        comment.setDeletedOn(LocalDateTime.now());
        repository.save(comment);
    }

    @Override
    public List<CommentDto> deleteComments(Long eventId, Long userId) {
        log.info("Delete comments. eventId = {}, userId = {}", eventId, userId);

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));

        List<Comment> comments = repository.findByEventAndAuthor(event, user);
        List<Comment> deleted = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getDeletedOn() == null) {
                continue;
            }
            comment.setDeletedOn(LocalDateTime.now());
            deleted.add(comment);
        }
        return deleted.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAdminComments(Long eventId, Boolean withDeleted, Integer from, Integer size) {
        log.info("Get admin comments. eventId = {}, withDeleted = {}", eventId, withDeleted);

        Pageable pageable = PageRequest.of(from / size, size);

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));

        List<Comment> comments;
        if (withDeleted) {
            comments = repository.findByEventOrderByCreatedOnDesc(event, pageable);
        } else {
            comments = repository.findByEventAndDeletedOnNullOrderByCreatedOnDesc(event, pageable);
        }
        return comments.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClaimDto addClaim(Long userId, Long commentId) {
        log.info("Add claim. userId = {}, commentId = {}", userId, commentId);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User not found. userId = %d", userId)));
        if (Boolean.TRUE.equals(user.getBanned())) {
            throw new ForbiddenException("You are banned");
        }

        Comment comment = repository.findById(commentId).orElseThrow(
                () -> new NotFoundException(String.format("Comment not found. commentId = %d", commentId)));
        if (comment.getDeletedOn() != null) {
            throw new BadRequestException(String.format("Comment is deleted. commentId = %d", commentId));
        }
        if (Objects.equals(comment.getAuthor(), user)) {
            throw new ForbiddenException("It's your comment. Are you crazy?");
        }

        Optional<Claim> optionalClaim = claimRepository.findByAuthorAndComment(user, comment);
        if (optionalClaim.isPresent()) {
            throw new BadRequestException(String.format("You are already claimed. userId = %d commentId = %d", userId, commentId));
        }

        Claim claim = new Claim();
        claim.setAuthor(user);
        claim.setComment(comment);
        claim.setState(ClaimState.PENDING);
        claim.setCreatedOn(LocalDateTime.now());

        return claimMapper.toDto(claimRepository.save(claim));
    }

    @Override
    public List<ClaimDto> getAdminClaims(@Nullable Long eventId, Boolean onlyPending, Integer from, Integer size) {
        log.info("Get admin claims. eventId = {}, onlyPending = {}", eventId, onlyPending);

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "CreatedOn"));

        List<Claim> claims;

        if (eventId == null) {
            if (Boolean.TRUE.equals(onlyPending)) {
                claims = claimRepository.findByState(ClaimState.PENDING, pageable);
            } else {
                claims = claimRepository.findAll(pageable).toList();
            }
        } else {
            Event event = eventRepository.findById(eventId).orElseThrow(
                    () -> new NotFoundException(String.format("Event not found. eventId = %d", eventId)));
            if (Boolean.TRUE.equals(onlyPending)) {
                claims = claimRepository.findByStateAndComment_Event(ClaimState.PENDING, event, pageable);
            } else {
                claims = claimRepository.findByComment_Event(event, pageable);
            }
        }

        return claims.stream().map(claimMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ClaimDto processClaim(Long claimId, ClaimState claimState) {
        log.info("Process claim. claimId = {}, claimState = {}", claimId, claimState);

        if (!ClaimState.APPROVED.equals(claimState) && !ClaimState.REJECTED.equals(claimState)) {
            throw new BadRequestException("Only APPROVED or REJECTED status allowed");
        }

        Claim claim = claimRepository.findById(claimId).orElseThrow(
                () -> new NotFoundException(String.format("Claim not found. claimId = %d", claimId)));
        if (!ClaimState.PENDING.equals(claim.getState())) {
            throw new BadRequestException("Claim must be in pending status");
        }

        claim.setState(claimState);
        claim.setAllowedOn(LocalDateTime.now());

        if (ClaimState.APPROVED.equals(claimState)) {
            Comment comment = claim.getComment();
            comment.setDeletedOn(LocalDateTime.now());
            repository.save(comment);
        }

        return claimMapper.toDto(claimRepository.save(claim));
    }
}
