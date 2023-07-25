package ru.practicum.ewm.main.compilations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.compilations.dto.CompilationDto;
import ru.practicum.ewm.main.compilations.service.CompilationService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/compilations")
@RestController
public class PublicCompilatonController {
    private final CompilationService service;

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) {
        CompilationDto result = service.getById(compId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<CompilationDto> result = service.getCompilations(pinned, from, size);
        return ResponseEntity.ok(result);
    }
}
