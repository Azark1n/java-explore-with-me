package ru.practicum.ewm.stat.service;

import ru.practicum.ewm.stat.dto.HitDto;
import ru.practicum.ewm.stat.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    HitDto addHit(HitDto hitDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
