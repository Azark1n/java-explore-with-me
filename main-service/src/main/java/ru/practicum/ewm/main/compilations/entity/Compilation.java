package ru.practicum.ewm.main.compilations.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm.main.event.entity.Event;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotNull
    @Column(nullable = false, unique = true, length = 50)
    String title;

    @NotNull
    Boolean pinned;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "compilation_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Event> events;
}