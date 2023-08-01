package ru.practicum.ewm.main.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.service.CategoryService;

import java.util.List;

@Tag(name = "Public: Категории", description = "Публичный API для работы с категориями")
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class PublicCategoryController {
    private final CategoryService service;

    @Operation(summary = "Получение категорий")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<CategoryDto> result = service.getAll(from, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получение информации о категории по её идентификатору")
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategories(@PathVariable Long catId) {
        CategoryDto result = service.getById(catId);
        return ResponseEntity.ok(result);
    }
}
