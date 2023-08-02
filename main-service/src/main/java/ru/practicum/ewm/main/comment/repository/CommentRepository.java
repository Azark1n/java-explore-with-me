package ru.practicum.ewm.main.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.comment.entity.Comment;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventAndAuthor(Event event, User author);

    List<Comment> findByEventAndDeletedOnNullOrderByCreatedOnDesc(Event event, Pageable pageable);

    List<Comment> findByEventOrderByCreatedOnDesc(Event event, Pageable pageable);

    Optional<Comment> findFirstByEventOrderByCreatedOnDesc(Event event);
}