package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.category.CategoryHomeDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductHomeFeaturedDto;
import az.edu.itbrains.pharmancy.services.CategoryService;
import az.edu.itbrains.pharmancy.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public HomeController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @GetMapping("/")
    public String index(Model model){
        List<ProductHomeFeaturedDto> productHomeFeaturedDtoList = productService.getHomeFeaturedProducts();
        model.addAttribute("featuredProducts",productHomeFeaturedDtoList);
        return "index.html";
    }
    @GetMapping("/shop")
    public String shop(){
        return "shop.html";
    }
    @GetMapping("/about")
    public String about(){
        return "about.html";
    }
}
