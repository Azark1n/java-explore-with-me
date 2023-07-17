package ru.practicum.evm.stat.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.evm.stat.dto.HitDto;
import ru.practicum.evm.stat.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    HitDto addHit(HitDto hitDto);

    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
