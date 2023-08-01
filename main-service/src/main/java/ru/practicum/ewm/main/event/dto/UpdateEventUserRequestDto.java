package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.main.CommonUtils;
import ru.practicum.ewm.main.event.common.EventUserStateAction;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Jacksonized
@Value
public class UpdateEventUserRequestDto implements Serializable {
    @Length(min = 20, max = 2000)
    String annotation;

    Long category;

    @Length(min = 20, max = 7000)
    String description;

    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration;

    EventUserStateAction stateAction;

    @Length(min = 3, max = 120)
    String title;
}