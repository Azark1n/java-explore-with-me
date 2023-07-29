package ru.practicum.ewm.main.comment.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.comment.dto.CommentDto;
import ru.practicum.ewm.main.comment.entity.Comment;
import ru.practicum.ewm.main.user.mapper.UserMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface CommentMapper {
    @Mapping(source = "eventId", target = "event.id")
    Comment toEntity(CommentDto commentDto);

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(expression = "java(comment.getClaims().size())", target = "claimCount")
    CommentDto toDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "eventId", target = "event.id")
    Comment partialUpdate(CommentDto commentDto, @MappingTarget Comment comment);
}