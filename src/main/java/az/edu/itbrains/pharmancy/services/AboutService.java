package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.about.AboutCreateDto;
import az.edu.itbrains.pharmancy.dtos.about.AboutDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AboutService {

    List<AboutDto> getAbout();
    void createAbout(AboutCreateDto aboutCreateDto, MultipartFile image);

    void delete(Long id);
}
