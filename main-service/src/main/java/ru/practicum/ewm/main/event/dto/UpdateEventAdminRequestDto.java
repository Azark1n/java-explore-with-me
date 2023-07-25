package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.main.CommonUtils;
import ru.practicum.ewm.main.event.common.EventAdminStateAction;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Value
public class UpdateEventAdminRequestDto implements Serializable {
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

    EventAdminStateAction stateAction;

    @Length(min = 3, max = 120)
    String title;
}