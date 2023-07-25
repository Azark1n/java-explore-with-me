package ru.practicum.ewm.main.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.event.dto.ParticipationRequestDto;
import ru.practicum.ewm.main.event.entity.ParticipationRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParticipationRequestMapper {
    @Mapping(target = "requester", source = "requester.id")
    @Mapping(target = "event", source = "event.id")
    ParticipationRequestDto toDto(ParticipationRequest participationRequest);
}