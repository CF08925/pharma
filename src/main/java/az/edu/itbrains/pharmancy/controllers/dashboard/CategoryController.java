package az.edu.itbrains.pharmancy.controllers.dashboard;


import az.edu.itbrains.pharmancy.dtos.category.CategoryCreateDto;
import az.edu.itbrains.pharmancy.dtos.category.CategoryDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductCreateDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductDto;
import az.edu.itbrains.pharmancy.services.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/categories")
    public String Products(Model model){
        List<CategoryDto> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        return "dashboard/categories/index.html";
    }

    @GetMapping("/categories/create")
    public String create(Model model){
        model.addAttribute("categoryCreateDto", new CategoryCreateDto());
        return "dashboard/categories/create.html";
    }

    @PostMapping("/categories/create")
    public String create(@Valid CategoryCreateDto categoryCreateDto, BindingResult bindingResult, org.springframework.web.multipart.MultipartFile image){
        if(bindingResult.hasErrors()){
            return "/dashboard/categories/create";
        }
        categoryService.createCategory(categoryCreateDto);
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String delete(@PathVariable Long id){
        categoryService.delete(id);
        return "redirect:/admin/categories";
    }

}
