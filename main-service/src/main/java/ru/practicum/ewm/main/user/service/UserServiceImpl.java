package ru.practicum.ewm.main.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.user.dto.NewUserRequest;
import ru.practicum.ewm.main.user.dto.UserDto;
import ru.practicum.ewm.main.user.entity.User;
import ru.practicum.ewm.main.user.mapper.UserMapper;
import ru.practicum.ewm.main.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        log.info("Create new user: {}", newUserRequest);

        User user = mapper.toEntity(newUserRequest);
        return mapper.toDto(repository.save(user));
    }

    @Override
    public void deleteById(Long userId) {
        log.info("Delete user by id: {}", userId);

        repository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Get users by id: {}", ids);

        Pageable pageable = PageRequest.of(from / size, size);

        return repository.findByIdIn(ids, pageable).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsers(Integer from, Integer size) {
        log.info("Get all users");

        Pageable pageable = PageRequest.of(from / size, size);

        return repository.findAll(pageable).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
