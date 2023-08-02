package ru.practicum.ewm.main.compilation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Jacksonized
@Value
public class UpdateCompilationRequest implements Serializable {
    @Length(min = 1, max = 50)
    String title;

    @Builder.Default
    Boolean pinned = false;

    List<Long> events;
}