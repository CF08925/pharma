package az.edu.itbrains.pharmancy.controllers.dashboard;

import az.edu.itbrains.pharmancy.dtos.product.ProductCreateDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductHomeFeaturedDto;
import az.edu.itbrains.pharmancy.services.ProductService;
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
public class ProductController {
   private final ProductService productService;
   private final ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/products")
    public String Products(Model model){
        List<ProductHomeFeaturedDto> products = productService.getHomeFeaturedProducts();
        model.addAttribute("products", products);
        return "dashboard/products/index.html";
    }

//    @GetMapping("/Products/create")
//    public String create(Model model){
//        model.addAttribute("ProductCreateDto", new productCreateDto());
//        return "dashboard/products/create.html";
//    }

//    @PostMapping("/Products/create")
//    public String create(@Valid ProductCreateDto productCreateDto, BindingResult bindingResult, org.springframework.web.multipart.MultipartFile image){
//        if(bindingResult.hasErrors()){
//            return "/dashboard/products/create";
//        }
//        ProductService.createProduct(productCreateDto, image);
//        return "redirect:/admin/products";
//    }

    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable Long id){
        productService.delete(id);
        return "redirect:/admin/products";
    }

}
