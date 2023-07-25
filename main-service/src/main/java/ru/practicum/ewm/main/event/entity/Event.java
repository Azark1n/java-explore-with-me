package ru.practicum.ewm.main.event.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm.main.category.entity.Category;
import ru.practicum.ewm.main.event.common.EventState;
import ru.practicum.ewm.main.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(nullable = false, length = 2000)
    String annotation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    Category category;

    @Column(nullable = false)
    LocalDateTime createdOn;

    @Column(nullable = false, length = 7000)
    String description;

    @Column(nullable = false)
    LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User initiator;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "location_id")
    Location location;

    @Column(nullable = false)
    Boolean paid;

    @Column(nullable = false)
    Integer participantLimit;

    LocalDateTime publishedOn;

    @Column(nullable = false)
    Boolean requestModeration;

    @Column(nullable = false)
    @Enumerated
    EventState state;

    @Column(nullable = false)
    String title;
}