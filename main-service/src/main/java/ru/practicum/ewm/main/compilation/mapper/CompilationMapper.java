package ru.practicum.ewm.main.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.compilation.dto.CompilationDto;
import ru.practicum.ewm.main.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.ewm.main.compilation.entity.Compilation;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.event.mapper.EventMapper;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = EventMapper.class)
public interface CompilationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", source = "events")
    Compilation partialUpdate(UpdateCompilationRequest compilationDto, @MappingTarget Compilation compilation, List<Event> events);

    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "events", source = "events")
    Compilation toEntity(NewCompilationDto newCompilationDto, List<Event> events);
}