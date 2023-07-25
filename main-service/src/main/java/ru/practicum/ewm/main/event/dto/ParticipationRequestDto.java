package ru.practicum.ewm.main.event.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.event.common.ParticipationRequestState;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Jacksonized
@Value
public class ParticipationRequestDto implements Serializable {
    public interface Create {}

    @Null(groups = Create.class)
    Long id;

    @NotNull
    @Min(1)
    Long requester;

    @Null(groups = Create.class)
    Long event;

    @NotNull
    LocalDateTime created;

    @NotNull
    ParticipationRequestState status;
}