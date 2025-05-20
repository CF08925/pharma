package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.basket.BasketAddDto;
import az.edu.itbrains.pharmancy.dtos.basket.BasketDto;
import az.edu.itbrains.pharmancy.services.BasketService;
import az.edu.itbrains.pharmancy.services.CategoryService;
import az.edu.itbrains.pharmancy.services.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class CartController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BasketService basketService;

    public CartController(ProductService productService, CategoryService categoryService, BasketService basketService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.basketService = basketService;
    }


    @PostMapping("/cart/{id}")
    public String addToCart(Principal principal, BasketAddDto basketAddDto, @PathVariable Long id){
        if (principal == null){
            return "redirect:/login";
        }
        basketAddDto.setProductId(id);
        boolean result = basketService.addToCart(principal.getName(), basketAddDto);

        if (result == true){
            return "redirect:/cart";
        }
        return "redirect:/detail/"+basketAddDto.getProductId();
    }

    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public String cart(Principal principal, Model model){

        List<BasketDto> basketDtoList = basketService.getUserBaskets();
        model.addAttribute("baskets", basketDtoList);
        return "cart.html";
    }
}
