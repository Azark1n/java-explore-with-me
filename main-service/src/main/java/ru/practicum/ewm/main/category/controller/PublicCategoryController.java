package ru.practicum.ewm.main.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.service.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class PublicCategoryController {
    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<CategoryDto> result = service.getAll(from, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategories(@PathVariable Long catId) {
        CategoryDto result = service.getById(catId);
        return ResponseEntity.ok(result);
    }
}
