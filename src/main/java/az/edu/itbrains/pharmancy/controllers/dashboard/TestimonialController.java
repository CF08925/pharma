package az.edu.itbrains.pharmancy.controllers.dashboard;


import az.edu.itbrains.pharmancy.dtos.about.AboutCreateDto;
import az.edu.itbrains.pharmancy.dtos.about.AboutDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialCreateDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialDto;
import az.edu.itbrains.pharmancy.services.TestimonialService;
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
public class TestimonialController {
    private final TestimonialService testimonialService;
    private final ModelMapper modelMapper;

    public TestimonialController(TestimonialService testimonialService, ModelMapper modelMapper) {
        this.testimonialService = testimonialService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/testimonials")
    public String Testimonial(Model model){
        List<TestimonialDto> testimonialDtos = testimonialService.getTestimonials();
        model.addAttribute("testimonials", testimonialDtos);
        return "dashboard/testimonials/index.html";
    }

    @GetMapping("/testimonials/create")
    public String create(Model model){
        model.addAttribute("testimonialCreateDto", new TestimonialCreateDto());
        return "dashboard/testimonials/create.html";
    }

    @PostMapping("/testimonials/create")
    public String create(@Valid TestimonialCreateDto testimonialCreateDto, BindingResult bindingResult, org.springframework.web.multipart.MultipartFile image){
        if(bindingResult.hasErrors()){
            return "/dashboard/testimonials/create";
        }
        testimonialService.createTestimonial(testimonialCreateDto, image);
        return "redirect:/admin/testimonials";
    }

    @PostMapping("/testimonials/delete/{id}")
    public String delete(@PathVariable Long id){
        testimonialService.delete(id);
        return "redirect:/testimonials";
    }
}
