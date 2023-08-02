package ru.practicum.ewm.main.compilation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.main.compilation.service.CompilationService;

import javax.validation.Valid;

@Tag(name = "Admin: Подборки событий", description = "API для работы с подборками событий")
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@RestController
public class AdminCompilationController {
    private final CompilationService service;

    @Operation(summary = "Добавление новой подборки (подборка может не содержать событий)")
    @PostMapping
    public ResponseEntity<CompilationDto> create(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        CompilationDto result = service.create(newCompilationDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Удаление подборки")
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long compId) {
        service.deleteById(compId);
    }

    @Operation(summary = "Обновить информацию о подборке")
    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> patch(@PathVariable Long compId, @RequestBody @Valid UpdateCompilationRequest patch) {
        CompilationDto result = service.patch(compId, patch);

        return ResponseEntity.ok(result);
    }
}
