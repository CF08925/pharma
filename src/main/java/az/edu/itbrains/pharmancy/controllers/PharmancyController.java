package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.category.CategoryDto;
import az.edu.itbrains.pharmancy.dtos.category.CategoryHomeDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductDetailDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductRelatedDto;
import az.edu.itbrains.pharmancy.services.CategoryService;
import az.edu.itbrains.pharmancy.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PharmancyController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public PharmancyController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping("/shop-single/{id}")
    public String detail(@PathVariable Long id, Model model){
        ProductDetailDto productDetailDto = productService.getDetailProductById(id);


        model.addAttribute("productDetail", productDetailDto);

        return "shop-single.html";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact.html";
    }

    @GetMapping("/cart")
    public String cart(){
        return "cart.html";
    }

}
