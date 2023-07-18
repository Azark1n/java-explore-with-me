package ru.practicum.evm.stat.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatsDto {
    String app;
    String uri;
    long hits;
}
