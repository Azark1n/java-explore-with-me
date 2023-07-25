package ru.practicum.ewm.stat.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Hits")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotEmpty
    @NotNull
    @Column(name = "app")
    private String app;

    @NotEmpty
    @NotNull
    @Column(name = "uri")
    private String uri;

    @NotEmpty
    @NotNull
    @Column(name = "ip")
    private String ip;

    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
