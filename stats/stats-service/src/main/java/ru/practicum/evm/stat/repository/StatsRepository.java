package ru.practicum.evm.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.evm.stat.dto.StatsDto;
import ru.practicum.evm.stat.entity.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit,Long> {
    @Query("SELECT new ru.practicum.evm.stat.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN (?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsDto> getStatsByPeriodAndUriDistinctIp(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT new ru.practicum.evm.stat.dto.StatsDto(h.app, h.uri, COUNT(h.id)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN (?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<StatsDto> getStatsByPeriodAndUri(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT new ru.practicum.evm.stat.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsDto> getStatsByPeriodDistinctIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.evm.stat.dto.StatsDto(h.app, h.uri, COUNT(h.id)) " +
            "FROM Hit AS h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<StatsDto> getStatsByPeriod(LocalDateTime start, LocalDateTime end);

}
