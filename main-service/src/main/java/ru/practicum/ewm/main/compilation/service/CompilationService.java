package ru.practicum.ewm.main.compilation.service;

import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    CompilationDto getById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto patch(Long compId, UpdateCompilationRequest patch);
}
