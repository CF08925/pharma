package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.about.AboutDto;
import az.edu.itbrains.pharmancy.dtos.category.CategoryHomeDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductHomeFeaturedDto;
import az.edu.itbrains.pharmancy.services.AboutService;
import az.edu.itbrains.pharmancy.services.CategoryService;
import az.edu.itbrains.pharmancy.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final AboutService aboutService;



    public HomeController(CategoryService categoryService, ProductService productService, AboutService aboutService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.aboutService = aboutService;
    }


    @GetMapping("/")
    public String index(Model model){
        List<ProductHomeFeaturedDto> productHomeFeaturedDtoList = productService.getHomeFeaturedProducts();
        List<CategoryHomeDto> categories = categoryService.getHomeCategories();

        model.addAttribute("featuredProducts", productHomeFeaturedDtoList);
        model.addAttribute("categories", categories);

        return "index.html";
    }

    // Add this method to handle all pages that need categories
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        List<CategoryHomeDto> categories = categoryService.getHomeCategories();
        model.addAttribute("categories", categories);
    }

//    @GetMapping("/shop")
//    public String shop(Model model){
//        List<ProductDto> productDtoList = productService.getAllProducts();
//        model.addAttribute("products", productDtoList);
//        return "shop.html";
//    }

    @GetMapping("/about")
    public String about(Model model){
        List<AboutDto> aboutDtoList = aboutService.getAbout();
        model.addAttribute("abouts", aboutDtoList);
        return "about.html";
    }

}
