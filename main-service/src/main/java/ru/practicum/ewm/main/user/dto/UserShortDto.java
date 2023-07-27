package ru.practicum.ewm.main.user.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Jacksonized
@Value
public class UserShortDto implements Serializable {
    Long id;

    @NotNull
    @NotEmpty
    String name;
}