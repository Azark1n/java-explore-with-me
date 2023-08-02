package ru.practicum.ewm.main.event.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewm.main.event.common.ParticipationRequestStateAction;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
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