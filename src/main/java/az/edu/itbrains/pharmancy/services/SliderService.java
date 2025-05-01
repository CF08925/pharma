package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.slider.SliderHomeDto;

import java.util.List;

public interface SliderService {

    List<SliderHomeDto> getHomeSlider();
}
