package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.category.CategoryDashboardDto;
import az.edu.itbrains.pharmancy.dtos.category.CategoryHomeDto;
import az.edu.itbrains.pharmancy.dtos.category.CategoryUpdateDto;
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

        return List.of();
    }

    @Override
    public CategoryUpdateDto getUpdatedCategory(Long id) {
        return null;
    }

    @Override
    public void updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) {

    }


}