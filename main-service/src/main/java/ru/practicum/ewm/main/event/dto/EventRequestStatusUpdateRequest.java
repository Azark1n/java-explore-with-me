package ru.practicum.ewm.main.event.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.event.common.ParticipationRequestStateAction;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Builder
@Jacksonized
@Value
public class EventRequestStatusUpdateRequest implements Serializable {
    @NotNull
    @NotEmpty
    List<Long> requestIds;

    @NotNull
    ParticipationRequestStateAction status;
}