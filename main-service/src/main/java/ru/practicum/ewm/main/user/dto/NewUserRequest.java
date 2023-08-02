package ru.practicum.ewm.main.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Jacksonized
@Value
public class NewUserRequest implements Serializable {
    @NotNull
    @NotBlank
    @Length(min = 6, max = 254)
    @Email
    String email;

    @NotNull
    @NotBlank
    @Length(min = 2, max = 250)
    String name;

    Boolean banned;
}