package ru.practicum.ewm.stat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HitDto {
    interface Create {}

    @Null(groups = {Create.class})
    Long id;

    @NotNull
    @NotBlank
    String app;

    @NotNull
    @NotBlank
    String uri;

    @NotNull
    @NotBlank
    String ip;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp;
}
