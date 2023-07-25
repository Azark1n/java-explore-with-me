package ru.practicum.ewm.main.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.event.dto.LocationDto;
import ru.practicum.ewm.main.event.entity.Location;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Location partialUpdate(LocationDto locationDto, @MappingTarget Location location);

    LocationDto toDto(Location location);

    Location toEntity(LocationDto locationDto);
}