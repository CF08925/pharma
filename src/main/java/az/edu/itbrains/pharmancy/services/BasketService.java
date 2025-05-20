package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.basket.BasketAddDto;
import az.edu.itbrains.pharmancy.dtos.basket.BasketDto;

import java.util.List;
import java.util.Map;

public interface BasketService {

    boolean addToCart(String name, BasketAddDto basketAddDto);

    List<BasketDto> getUserBaskets();




}
