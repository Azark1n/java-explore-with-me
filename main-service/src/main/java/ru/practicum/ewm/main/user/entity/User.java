package ru.practicum.ewm.main.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(unique = true, length = 254, nullable = false)
    String email;

    @Column(nullable = false, unique = true, length = 250)
    String name;

    @Column(name = "banned")
    Boolean banned;
}