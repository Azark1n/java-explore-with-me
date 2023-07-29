package ru.practicum.ewm.main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.comment.dto.ClaimDto;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.dto.NewCommentDto;
import ru.practicum.ewm.main.comment.service.CommentService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@RestController
public class PrivateCommentController {
    private final CommentService service;

    @PostMapping("/events/{eventId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long userId, @PathVariable Long eventId,
                                                 @RequestBody @Valid NewCommentDto newCommentDto) {
        CommentDto result = service.addComment(userId, eventId, newCommentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> patchComment(@PathVariable Long userId, @PathVariable Long commentId,
                                                   @RequestBody @Valid NewCommentDto newCommentDto) {
        CommentDto result = service.patchComment(userId, commentId, newCommentDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/comments/{commentId}/claims")
    public ResponseEntity<ClaimDto> addClaim(@PathVariable Long userId, @PathVariable Long commentId) {
        ClaimDto result = service.addClaim(userId, commentId);

        return ResponseEntity.ok(result);
    }
}
