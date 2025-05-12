package az.edu.itbrains.pharmancy.controllers.dashboard;


import az.edu.itbrains.pharmancy.dtos.about.AboutCreateDto;
import az.edu.itbrains.pharmancy.dtos.about.AboutDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductCreateDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductDto;
import az.edu.itbrains.pharmancy.services.AboutService;
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
public class AboutController {
    private final AboutService aboutService;
    private final ModelMapper modelMapper;

    public AboutController(AboutService aboutService, ModelMapper modelMapper) {
        this.aboutService = aboutService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/about")
    public String About(Model model){
        List<AboutDto> aboutDtoList = aboutService.getAbout();
        model.addAttribute("abouts", aboutDtoList);
        return "dashboard/about/index.html";
    }

    @GetMapping("/about/create")
    public String create(Model model){
        model.addAttribute("aboutCreateDto", new AboutCreateDto());
        return "dashboard/about/create.html";
    }

    @PostMapping("/about/create")
    public String create(@Valid AboutCreateDto aboutCreateDto, BindingResult bindingResult, org.springframework.web.multipart.MultipartFile image){
        if(bindingResult.hasErrors()){
            return "/dashboard/about/create";
        }
        aboutService.createAbout(aboutCreateDto, image);
        return "redirect:/admin/about";
    }

    @PostMapping("/about/delete/{id}")
    public String delete(@PathVariable Long id){
        aboutService.delete(id);
        return "redirect:/admin/about";
    }

}
