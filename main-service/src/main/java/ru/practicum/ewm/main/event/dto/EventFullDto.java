package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.CommonUtils;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.user.dto.UserShortDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Jacksonized
@Value
public class EventFullDto implements Serializable {
    public interface Create {}

    @Null(groups = Create.class)
    Long id;

    @NotNull
    @NotEmpty
    String annotation;

    @NotNull
    CategoryDto category;

    @Null(groups = Create.class)
    Long confirmedRequests;

    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime createdOn;

    @NotNull
    @NotEmpty
    String description;

    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime eventDate;

    @NotNull
    UserShortDto initiator;

    @NotNull
    LocationDto location;

    @NotNull
    Boolean paid;

    Integer participantLimit;

    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime publishedOn;

    @NotNull
    Boolean requestModeration;

    @NotNull
    EventState state;

    @NotNull
    @NotEmpty
    String title;

    @Null(groups = Create.class)
    Long views;
}