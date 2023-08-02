package ru.practicum.ewm.main.comment.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.comment.dto.ClaimDto;
import ru.practicum.ewm.main.comment.entity.Claim;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClaimMapper {
    @Mapping(source = "commentId", target = "comment.id")
    Claim toEntity(ClaimDto claimDto);

    @InheritInverseConfiguration(name = "toEntity")
    ClaimDto toDto(Claim claim);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Claim partialUpdate(ClaimDto claimDto, @MappingTarget Claim claim);
}