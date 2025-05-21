package az.edu.itbrains.pharmancy.controllers;


import az.edu.itbrains.pharmancy.dtos.basket.BasketDto;
import az.edu.itbrains.pharmancy.services.BasketService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {
    private final BasketService basketService;
    private final ModelMapper modelMapper;


    @GetMapping("/checkout")
    public String checkout(Model model){
        List<BasketDto> basketDtoList = basketService.getUserBaskets();
        model.addAttribute("baskets", basketDtoList);
        return "checkout.html";
    }
}
