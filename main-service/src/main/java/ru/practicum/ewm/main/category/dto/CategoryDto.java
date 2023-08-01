package ru.practicum.ewm.main.category.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Jacksonized
@Value
public class CategoryDto implements Serializable {
    Long id;

    @NotNull
    @Length(min = 1, max = 50)
    String name;
}