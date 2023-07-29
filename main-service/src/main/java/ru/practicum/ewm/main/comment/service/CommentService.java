package ru.practicum.ewm.main.comment.service;

import ru.practicum.ewm.main.comment.common.ClaimState;
import ru.practicum.ewm.main.comment.dto.ClaimDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto patchComment(Long userId, Long commentId, NewCommentDto newCommentDto);

    List<CommentDto> getComments(Long eventId, Integer from, Integer size);

    void deleteComment(Long commentId);

    List<CommentDto> deleteComments(Long eventId, Long userId);

    List<CommentDto> getAdminComments(Long eventId, Boolean withDeleted, Integer from, Integer size);

    ClaimDto addClaim(Long userId, Long commentId);

    List<ClaimDto> getAdminClaims(Long eventId, Boolean onlyPending, Integer from, Integer size);

    ClaimDto processClaim(Long claimId, ClaimState claimState);
}
