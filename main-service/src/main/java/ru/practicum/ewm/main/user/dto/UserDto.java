package ru.practicum.ewm.main.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Jacksonized
@Value
public class UserDto implements Serializable {
    Long id;

    @NotNull
    @Email
    @NotEmpty
    String email;

    @NotNull
    @NotEmpty
    String name;

    Boolean banned;
}