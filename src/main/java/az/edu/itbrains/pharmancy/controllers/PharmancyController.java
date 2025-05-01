package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.product.ProductDetailDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductRelatedDto;
import az.edu.itbrains.pharmancy.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PharmancyController {

    private final ProductService productService;


    @GetMapping("/shop-single/{id}")
    public String detail(@PathVariable Long id, Model model){
        ProductDetailDto productDetailDto = productService.getDetailProductById(id);
        List<ProductRelatedDto> productRelatedDtoList = productService.getRelatedProducts(id);

        model.addAttribute("product", productDetailDto);
        model.addAttribute("products", productRelatedDtoList);
        return "shop-single.html";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact.html";
    }

}
