package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.CommonUtils;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.user.dto.UserShortDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Value
public class EventShortDto implements Serializable {
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
    LocalDateTime eventDate;

    @NotNull
    UserShortDto initiator;

    @NotNull
    Boolean paid;

    @NotNull
    @NotEmpty
    String title;

    @Null(groups = Create.class)
    Long views;
}