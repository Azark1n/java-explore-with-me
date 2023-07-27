package ru.practicum.ewm.main.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.main.event.common.ParticipationRequestState;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.event.entity.ParticipationRequest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByIdIn(List<Long> requestIds);

    List<ParticipationRequest> findByEvent(Event event);

    Optional<ParticipationRequest> findByEvent_IdAndRequester_Id(Long id, Long id1);

    List<ParticipationRequest> findByRequester_Id(Long id);

    @Query("select p.event.id, count(p.id) " +
            "from ParticipationRequest p where p.status = ?1 and p.event in ?2 " +
            "group by p.event.id")
    List<Object[]> getCountRequestsEqualStatusAndEventIn(ParticipationRequestState status, Collection<Event> events);
}