package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.category.*;
import jakarta.validation.Valid;

import java.util.List;

public interface CategoryService {
    List<CategoryHomeDto> getHomeCategories();
    List<CategoryDashboardDto> getDashboardCategories();
    CategoryUpdateDto getUpdatedCategory(Long id);
    void updateCategory(Long id, CategoryUpdateDto categoryUpdateDto);
    List<CategoryDto> getCategories();

    void createCategory(@Valid CategoryCreateDto categoryCreateDto);

    void delete(Long id);
}
