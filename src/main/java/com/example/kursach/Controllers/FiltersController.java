package com.example.kursach.Controllers;


import com.example.kursach.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/filters")
public class FiltersController
{
    private final ProductService productService;


    @GetMapping
    public String mainMenu( Model model) {

        getMinMaxPerfumePrice(model);
        model.addAttribute("url", "/filters");

        return "filters";
    }
    private void getMinMaxPerfumePrice(Model model) {
        BigDecimal minProductPrice = productService.minProductPrice();
        BigDecimal maxProductPrice = productService.maxProductPrice();

        model.addAttribute("minProductPrice", minProductPrice);
        model.addAttribute("maxProductPrice", maxProductPrice);
    }
}
