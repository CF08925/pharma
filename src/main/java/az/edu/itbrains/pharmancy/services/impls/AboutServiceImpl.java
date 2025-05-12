package az.edu.itbrains.pharmancy.services.impls;


import az.edu.itbrains.pharmancy.dtos.about.AboutCreateDto;
import az.edu.itbrains.pharmancy.dtos.about.AboutDto;
import az.edu.itbrains.pharmancy.models.About;
import az.edu.itbrains.pharmancy.models.Category;
import az.edu.itbrains.pharmancy.models.Product;
import az.edu.itbrains.pharmancy.repositories.AboutRepository;
import az.edu.itbrains.pharmancy.services.AboutService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AboutServiceImpl implements AboutService {
    private final AboutRepository aboutRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    @Override
    public List<AboutDto> getAbout() {
        List<About> findAbout = aboutRepository.findAll();
        List<AboutDto> aboutDtos = findAbout.stream().map(about -> modelMapper.map(about, AboutDto.class)).collect(Collectors.toList());



        return aboutDtos;
    }

    @Override
    public void createAbout(AboutCreateDto aboutCreateDto, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("About image is required");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String photoUrl = (String) uploadResult.get("url");

            About about = new About();


            // Set other fields
            about.setAboutText(aboutCreateDto.getAboutText());
            about.setPhotoUrl(photoUrl);
            about.setVideoUrl(aboutCreateDto.getVideoUrl());

            aboutRepository.save(about);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (aboutRepository.existsById(id)) {
            aboutRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("about with ID " + id + " not found");
        }
    }
}
