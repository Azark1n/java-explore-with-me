package ru.practicum.ewm.main.compilation.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.event.dto.EventShortDto;

import java.io.Serializable;
import java.util.List;

@Builder
@Jacksonized
@Value
public class CompilationDto implements Serializable {
    Long id;

    String title;

    Boolean pinned;

    List<EventShortDto> events;
}