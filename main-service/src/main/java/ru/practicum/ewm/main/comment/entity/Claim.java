package ru.practicum.ewm.main.comment.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.comment.common.ClaimState;
import ru.practicum.ewm.main.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "claim")
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "comment_id", nullable = false)
    Comment comment;

    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;

    @Column(name = "state", nullable = false)
    ClaimState state;

    @Column(name = "allowed_on")
    LocalDateTime allowedOn;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "allowed_author_id")
    User allowedAuthor;
}