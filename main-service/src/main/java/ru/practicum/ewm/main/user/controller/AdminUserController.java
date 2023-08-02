package ru.practicum.ewm.main.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.user.dto.NewUserRequest;
import ru.practicum.ewm.main.user.dto.UserDto;
import ru.practicum.ewm.main.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Tag(name = "Admin: Пользователи", description = "API для работы с пользователями")
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@RestController
public class AdminUserController {
    private final UserService service;

    @Operation(summary = "Добавление нового пользователя")
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid NewUserRequest newUserRequest) {
        UserDto result = service.create(newUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Удаление пользователя")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long userId) {
        service.deleteById(userId);
    }

    @Operation(summary = "Получение информации о пользователях")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(required = false) List<Long> ids,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        List<UserDto> result = ids == null ? service.getUsers(from, size) : service.getUsers(ids, from, size);
        return ResponseEntity.ok(result);
    }
}
