package ru.practicum.ewm.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.main.CommonUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Value
public class NewEventDto implements Serializable {
    @NotNull
    @NotEmpty
    @Length(min = 20, max = 2000)
    String annotation;

    @NotNull
    Long category;

    @NotNull
    @NotEmpty
    @Length(min = 20, max = 7000)
    String description;

    @NotNull
    @JsonFormat(pattern = CommonUtils.DATE_FORMAT)
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    @Builder.Default
    Boolean paid = false;

    @PositiveOrZero
    @Builder.Default
    Integer participantLimit = 0;

    @Builder.Default
    Boolean requestModeration = true;

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 120)
    String title;
}