package ru.practicum.ewm.main.category.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Jacksonized
@Value
public class NewCategoryDto implements Serializable {
    @NotNull
    @NotBlank
    @Length(min = 1, max = 50)
    String name;
}