package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialCreateDto;
import az.edu.itbrains.pharmancy.dtos.testimonial.TestimonialDto;
import az.edu.itbrains.pharmancy.models.Testimonial;
import az.edu.itbrains.pharmancy.repositories.TestimonialRepository;
import az.edu.itbrains.pharmancy.services.TestimonialService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TestimonialServiceImpl implements TestimonialService {


    private final TestimonialRepository testimonialRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;


    @Override
    public List<TestimonialDto> getTestimonials() {
        List<Testimonial> testimonials = testimonialRepository.getAllTestimonials();
        List<TestimonialDto> testimonialDtos = testimonials.stream().map(testimonial -> modelMapper.map(testimonial, TestimonialDto.class)).collect(Collectors.toList());
        return testimonialDtos;
    }

    @Override
    public void createTestimonial(TestimonialCreateDto testimonialCreateDto, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Product image is required");
        }
        try{
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String photoUrl = (String) uploadResult.get("url");

            Testimonial testimonial = new Testimonial();
            testimonial.setName(testimonialCreateDto.getName());
            testimonial.setSurname(testimonialCreateDto.getSurname());
            testimonial.setPosition(testimonialCreateDto.getPosition());
            testimonial.setPhotoUrl(photoUrl);
            testimonial.setAbout(testimonialCreateDto.getAbout());
            testimonialRepository.save(testimonial);
        }catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (testimonialRepository.existsById(id)) {
            testimonialRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Testimonial with ID " + id + " not found");
        }
    }
}
