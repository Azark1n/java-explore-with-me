package ru.practicum.ewm.main.event.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Builder
@Jacksonized
@Value
public class LocationDto implements Serializable {
    Long id;
    Float lat;
    Float lon;
}