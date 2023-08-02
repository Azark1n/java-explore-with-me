package ru.practicum.ewm.main.comment.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.event.entity.Event;
import ru.practicum.ewm.main.user.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotEmpty
    @Column(nullable = false, length = 1024)
    String text;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;

    @Column(name = "edited_on")
    LocalDateTime editedOn;

    @Column(name = "deleted_on")
    LocalDateTime deletedOn;

    @ToString.Exclude
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Claim> claims = new ArrayList<>();
}
