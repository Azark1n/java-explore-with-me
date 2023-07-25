package ru.practicum.ewm.stat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.ewm.stat.dto.HitDto;
import ru.practicum.ewm.stat.entity.Hit;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HitMapper {
    Hit toEntity(HitDto dto);

    HitDto toDto(Hit hit);
}