package ru.practicum.ewm.main.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.comment.dto.ClaimDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;
import ru.practicum.ewm.main.comment.service.CommentService;

import javax.validation.Valid;

@Tag(name = "Private: Комментарии", description = "Закрытый API для работы с комментариями событий")
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@RestController
public class PrivateCommentController {
    private final CommentService service;

    @Operation(summary = "Добавление нового комментария к событию", description = "Комментировать возможно только опубликованные события. " +
            "Пользователь не должен быть забанен")
    @PostMapping("/events/{eventId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long userId, @PathVariable Long eventId,
                                                 @RequestBody @Valid NewCommentDto newCommentDto) {
        CommentDto result = service.addComment(userId, eventId, newCommentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Редактирование комментария к событию", description = "Редактирование возможно его автором, при следующих условиях:\n" +
            "* в течение 10 минут с момента создания\n" +
            "* не добавлен следующий комментарий\n" +
            "* на комментарий не пожаловались")
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> patchComment(@PathVariable Long userId, @PathVariable Long commentId,
                                                   @RequestBody @Valid NewCommentDto newCommentDto) {
        CommentDto result = service.patchComment(userId, commentId, newCommentDto);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Добавление жалобы на комментарий", description = "Пожаловаться можно на чужой комментарий, если он не удален и нет бана")
    @PostMapping("/comments/{commentId}/claims")
    public ResponseEntity<ClaimDto> addClaim(@PathVariable Long userId, @PathVariable Long commentId) {
        ClaimDto result = service.addClaim(userId, commentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
