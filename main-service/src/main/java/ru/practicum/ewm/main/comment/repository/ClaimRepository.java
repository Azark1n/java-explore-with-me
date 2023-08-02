package ru.practicum.ewm.main.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.comment.common.ClaimState;
import ru.practicum.ewm.main.comment.entity.Claim;
import ru.practicum.ewm.main.comment.entity.Comment;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByComment_Event(Event event, Pageable pageable);

    List<Claim> findByStateAndComment_Event(ClaimState state, Event event, Pageable pageable);

    List<Claim> findByState(ClaimState state, Pageable pageable);

    Optional<Claim> findByAuthorAndComment(User author, Comment comment);

    Optional<Claim> findByComment(Comment comment);
}