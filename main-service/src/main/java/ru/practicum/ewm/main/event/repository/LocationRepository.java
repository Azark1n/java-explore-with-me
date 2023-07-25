package ru.practicum.ewm.main.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.event.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}