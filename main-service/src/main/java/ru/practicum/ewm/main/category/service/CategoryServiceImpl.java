package ru.practicum.ewm.main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.category.dto.CategoryDto;
import ru.practicum.ewm.main.category.dto.NewCategoryDto;
import ru.practicum.ewm.main.category.entity.Category;
import ru.practicum.ewm.main.category.mapper.CategoryMapper;
import ru.practicum.ewm.main.category.repository.CategoryRepository;
import ru.practicum.ewm.main.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        log.info("Create new category: {}", newCategoryDto);

        Category saved = repository.save(mapper.toEntity(newCategoryDto));

        return mapper.toDto(saved);
    }

    @Override
    public void deleteById(Long catId) {
        log.info("Delete category with id = {}", catId);

        repository.deleteById(catId);
    }

    @Override
    public CategoryDto patch(Long catId, CategoryDto patch) {
        log.info("Patch category with id = {}, patch data: {}", catId, patch);

        Category category = repository.findById(catId).orElseThrow(
                () -> new NotFoundException(String.format("Category not found. catId = %d", catId)));

        Category patched = repository.save(mapper.partialUpdate(patch, category));

        return mapper.toDto(patched);
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Get all categories");

        Pageable pageable = PageRequest.of(from / size, size);

        return repository.findAll(pageable).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long catId) {
        log.info("Get category by id = {}", catId);

        Category category = repository.findById(catId).orElseThrow(
                () -> new NotFoundException(String.format("Category not found. catId = %d", catId)));

        return mapper.toDto(category);
    }
}
