package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.product.ProductDto;
import az.edu.itbrains.pharmancy.services.CategoryService;
import az.edu.itbrains.pharmancy.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ShopController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String shop(@RequestParam(required = false) Long categoryId, Model model) {
        List<ProductDto> products;

        if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getHomeCategories());
        model.addAttribute("selectedCategoryId", categoryId);

        return "shop.html";
    }
}
