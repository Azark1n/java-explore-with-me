package ru.practicum.evm.stat.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.evm.stat.dto.HitDto;
import ru.practicum.evm.stat.entity.Hit;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HitMapper {
    Hit toEntity(HitDto dto);
    HitDto toDto(Hit hit);
}
