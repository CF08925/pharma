package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.category.*;
import az.edu.itbrains.pharmancy.models.Category;
import az.edu.itbrains.pharmancy.repositories.CategoryRepository;
import az.edu.itbrains.pharmancy.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryHomeDto> getHomeCategories() {
        List<Category> findCategories = categoryRepository.findAll().stream().collect(Collectors.toList());
        List<CategoryHomeDto> categories = findCategories.stream().map(category -> modelMapper.map(category, CategoryHomeDto.class)).collect(Collectors.toList());

        return categories;
    }

    @Override
    public List<CategoryDashboardDto> getDashboardCategories() {
        List<Category> findCategories = categoryRepository.findAll();
        return null;
    }

    @Override
    public CategoryUpdateDto getUpdatedCategory(Long id) {
        return null;
    }

    @Override
    public void updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) {

    }

    @Override
    public List<CategoryDto> getCategories() {
        List<Category> findCategories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = findCategories.stream().map(category -> modelMapper.map(category , CategoryDto.class)).collect(Collectors.toList());
        return categoryDtos;
    }

    @Override
    public void createCategory(CategoryCreateDto categoryCreateDto) {
        Category category = new Category();
        category.setName(categoryCreateDto.getName());
    }

    @Override
    public void delete(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("category with ID " + id + " not found");
        }
    }


}