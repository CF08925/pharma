package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.about.AboutDto;
import az.edu.itbrains.pharmancy.dtos.category.CategoryHomeDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductHomeFeaturedDto;
import az.edu.itbrains.pharmancy.dtos.team.TeamDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialDto;
import az.edu.itbrains.pharmancy.models.Testimonial;
import az.edu.itbrains.pharmancy.services.*;
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
    private final TestimonialService testimonialService;
    private final TeamService teamService;

    public HomeController(CategoryService categoryService, ProductService productService, AboutService aboutService, TestimonialService testimonialService, TeamService teamService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.aboutService = aboutService;
        this.testimonialService = testimonialService;
        this.teamService = teamService;
    }


    @GetMapping("/")
    public String index(Model model){
        List<ProductHomeFeaturedDto> productHomeFeaturedDtoList = productService.getHomeFeaturedProducts();
        List<CategoryHomeDto> categories = categoryService.getHomeCategories();
        List<TestimonialDto> testimonialDtos = testimonialService.getTestimonials();
        model.addAttribute("testimonials", testimonialDtos);
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



    @GetMapping("/about")
    public String about(Model model){
        List<AboutDto> aboutDtoList = aboutService.getAbout();
        List<TeamDto> teamDtos = teamService.getTeams();
        model.addAttribute("abouts", aboutDtoList);
        model.addAttribute("teams", teamDtos);
        return "about.html";
    }

    @GetMapping("/receipt")
    public String receipt(){
        return "receipt.html";
    }

}
