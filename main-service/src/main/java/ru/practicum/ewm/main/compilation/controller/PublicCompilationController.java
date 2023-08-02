package ru.practicum.ewm.main.compilation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.service.CompilationService;

import java.util.List;

@Tag(name = "Public: Подборки событий", description = "Публичный API для работы с подборками событий")
@RequiredArgsConstructor
@RequestMapping("/compilations")
@RestController
public class PublicCompilationController {
    private final CompilationService service;

    @Operation(summary = "Получение подборки событий по его id")
    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) {
        CompilationDto result = service.getById(compId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получение подборок событий", description = "В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список")
    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<CompilationDto> result = service.getCompilations(pinned, from, size);
        return ResponseEntity.ok(result);
    }
}
