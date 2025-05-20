package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialCreateDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestimonialService {
    List<TestimonialDto> getTestimonials();

    void createTestimonial(@Valid TestimonialCreateDto testimonialCreateDto, MultipartFile image);

    void delete(Long id);
}
