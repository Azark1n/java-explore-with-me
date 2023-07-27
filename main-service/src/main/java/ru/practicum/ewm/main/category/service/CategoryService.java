package ru.practicum.ewm.main.category.service;

import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void deleteById(Long catId);

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long catId);

    CategoryDto patch(Long catId, CategoryDto patch);
}
