package ru.practicum.ewm.main.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.event.entity.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<Event> findByIdAndState(Long id, EventState state);

    List<Event> findByIdIn(Collection<Long> ids);

    List<Event> findByInitiator_Id(Long userId, Pageable pageable);

    Optional<Event> findByInitiator_IdAndId(Long userId, Long eventId);
}