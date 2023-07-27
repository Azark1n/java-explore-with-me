package ru.practicum.ewm.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stat.dto.HitDto;
import ru.practicum.ewm.stat.dto.StatsDto;
import ru.practicum.ewm.stat.entity.Hit;
import ru.practicum.ewm.stat.mapper.HitMapper;
import ru.practicum.ewm.stat.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatsRepository repository;
    private final HitMapper mapper;

    @Override
    public HitDto addHit(HitDto hitDto) {
        Hit hit = repository.save(mapper.toEntity(hitDto));
        return mapper.toDto(hit);
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (Boolean.TRUE.equals(unique)) {
                return repository.getStatsByPeriodDistinctIp(start, end);
            } else {
                return repository.getStatsByPeriod(start, end);
            }
        } else {
            if (Boolean.TRUE.equals(unique)) {
                return repository.getStatsByPeriodAndUriDistinctIp(start, end, uris);
            } else {
                return repository.getStatsByPeriodAndUri(start, end, uris);
            }
        }
    }
}
