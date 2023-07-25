package ru.practicum.ewm.main.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.dto.NewCategoryDto;
import ru.practicum.ewm.main.category.service.CategoryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@RestController
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        CategoryDto result = service.create(newCategoryDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long catId) {
        service.deleteById(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patch(@PathVariable Long catId, @RequestBody @Valid CategoryDto patch) {
        CategoryDto result = service.patch(catId, patch);

        return ResponseEntity.ok(result);
    }
}
