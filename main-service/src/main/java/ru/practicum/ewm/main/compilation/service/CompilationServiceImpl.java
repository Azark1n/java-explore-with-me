package ru.practicum.ewm.main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.main.compilation.entity.Compilation;
import ru.practicum.ewm.main.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.main.compilation.repository.CompilationRepository;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.event.repository.EventRepository;
import ru.practicum.ewm.main.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper mapper;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        log.info("Create new compilation: {}", newCompilationDto);

        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        }

        Compilation saved = repository.save(mapper.toEntity(newCompilationDto, events));

        return mapper.toDto(saved);
    }

    @Override
    public void deleteById(Long compId) {
        log.info("Delete compilation by id = {}", compId);

        repository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation not found. compId = %d", compId)));

        repository.deleteById(compId);
    }

    @Override
    public CompilationDto getById(Long compId) {
        log.info("Get compilation by id = {}", compId);

        Compilation compilation = repository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation not found. compId = %d", compId)));

        return mapper.toDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Get compilations. pinned = {}", pinned);

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Compilation> result;
        if (pinned != null) {
            result = repository.findByPinned(pinned, pageable);
        } else {
            result = repository.findAll(pageable);
        }
        return result.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto patch(Long compId, UpdateCompilationRequest patch) {
        log.info("Patch compilation by id = {}, {}", compId, patch);

        Compilation compilation = repository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation not found. compId = %d", compId)));

        List<Event> events = new ArrayList<>();
        if (patch.getEvents() != null) {
            events = eventRepository.findByIdIn(patch.getEvents());
        }

        Compilation patched = repository.save(mapper.partialUpdate(patch, compilation, events));

        return mapper.toDto(patched);
    }
}
