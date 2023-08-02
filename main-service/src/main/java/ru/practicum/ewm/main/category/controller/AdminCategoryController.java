package ru.practicum.ewm.main.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.dto.NewCategoryDto;
import ru.practicum.ewm.main.category.service.CategoryService;

import javax.validation.Valid;

@Tag(name = "Admin: Категории", description = "API для работы с категориями")
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@RestController
public class AdminCategoryController {
    private final CategoryService service;

    @Operation(summary = "Добавление новой категории")
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        CategoryDto result = service.create(newCategoryDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "Удаление категории")
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long catId) {
        service.deleteById(catId);
    }

    @Operation(summary = "Изменение категории")
    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patch(@PathVariable Long catId, @RequestBody @Valid CategoryDto patch) {
        CategoryDto result = service.patch(catId, patch);

        return ResponseEntity.ok(result);
    }
}
