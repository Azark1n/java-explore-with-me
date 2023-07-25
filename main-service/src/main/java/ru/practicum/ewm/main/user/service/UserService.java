package ru.practicum.ewm.main.user.service;

import ru.practicum.ewm.main.user.dto.NewUserRequest;
import ru.practicum.ewm.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    List<UserDto> getUsers(Integer from, Integer size);

    UserDto create(NewUserRequest newUserRequest);

    void deleteById(Long userId);
}
