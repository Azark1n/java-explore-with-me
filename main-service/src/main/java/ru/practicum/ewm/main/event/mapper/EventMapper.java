package ru.practicum.ewm.main.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.main.category.entity.Category;
import ru.practicum.ewm.main.category.mapper.CategoryMapper;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.event.dto.*;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.user.entity.User;
import ru.practicum.ewm.main.user.mapper.UserMapper;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, UserMapper.class, LocationMapper.class})
public interface EventMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "category", source = "category")
    Event toEntity(NewEventDto newEventDto, Category category, LocalDateTime createdOn, User initiator, EventState state);

    @Mapping(target = "confirmedRequests", source = "confirmedRequestsCount")
    @Mapping(target = "views", source = "viewsCount")
    EventFullDto toFullDto(Event event, Long confirmedRequestsCount, Long viewsCount);

    EventShortDto toShortDto(Event event);

    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(UpdateEventUserRequestDto patchDto, @MappingTarget Event event);

    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(UpdateEventAdminRequestDto patchDto, @MappingTarget Event event);
}