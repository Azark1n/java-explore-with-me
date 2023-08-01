package ru.practicum.ewm.main.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.service.CommentService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Tag(name = "Public: Комментарии")
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
@RestController
public class PublicCommentController {
    private final CommentService service;

    @Operation(summary = "Получение списка комментариев с пагинацией для определенного события")
    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long eventId,
                                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(name = "size", defaultValue = "10") @Positive @Max(50) Integer size) {
        List<CommentDto> result = service.getComments(eventId, from, size);

        return ResponseEntity.ok(result);
    }
}
