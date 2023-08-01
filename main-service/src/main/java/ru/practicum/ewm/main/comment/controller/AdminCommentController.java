package ru.practicum.ewm.main.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.comment.common.ClaimState;
import ru.practicum.ewm.main.comment.dto.ClaimDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.service.CommentService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Tag(name = "Admin: Комментарии")
@RequiredArgsConstructor
@RestController
public class AdminCommentController {
    private final CommentService service;

    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        service.deleteComment(commentId);
    }

    @Operation(summary = "Удаление всех комментариев события от определенного пользователя")
    @DeleteMapping("/admin/comments")
    public ResponseEntity<List<CommentDto>> deleteComments(@RequestParam Long eventId, @RequestParam Long userId) {
        List<CommentDto> result = service.deleteComments(eventId, userId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получение списка комментариев по событию с возможностью просмотра удаленных")
    @GetMapping("/admin/events/{eventId}/comments")
    public ResponseEntity<List<CommentDto>> getAdminComments(@PathVariable Long eventId, @RequestParam(defaultValue = "false") Boolean withDeleted,
                                                             @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                             @RequestParam(name = "size", defaultValue = "10") @Positive @Max(50) Integer size) {
        List<CommentDto> result = service.getAdminComments(eventId, withDeleted, from, size);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получение списка жалоб", description = "В случае указания eventId - отбор будет производиться по указанному событию.\n" +
            "Параметр onlyPending - отвечает за отбор по статусу (выводить все или только нуждающиеся в обработке)")
    @GetMapping("/admin/claims")
    public ResponseEntity<List<ClaimDto>> getAdminClaims(@RequestParam(required = false) Long eventId, @RequestParam(defaultValue = "true") Boolean onlyPending,
                                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") @Positive @Max(50) Integer size) {
        List<ClaimDto> result = service.getAdminClaims(eventId, onlyPending, from, size);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Изменение статуса жалобы", description = "Возможен перевод в следующие статусы: APPROVED, REJECTED")
    @PatchMapping("/admin/claims/{claimId}")
    public ResponseEntity<ClaimDto> patchClaim(@PathVariable Long claimId, @RequestParam ClaimState claimState) {
        ClaimDto result = service.processClaim(claimId, claimState);

        return ResponseEntity.ok(result);
    }
}