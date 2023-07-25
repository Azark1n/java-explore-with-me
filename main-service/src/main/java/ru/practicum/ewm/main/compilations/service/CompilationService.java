package ru.practicum.ewm.main.compilations.service;

import ru.practicum.ewm.main.compilations.dto.CompilationDto;
import ru.practicum.ewm.main.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilations.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);

    CompilationDto create(NewCompilationDto newCompilationDto);

    void deleteById(Long compId);

    CompilationDto patch(Long compId, UpdateCompilationRequest patch);
}
