package com.example.kursach.Controllers;

import com.example.kursach.Model.Product;
import com.example.kursach.Services.ProductService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Data
public class MainController {
private final ProductService productService;
    @GetMapping("/")
    public String greeting(Model model) {
        List<Product> productList=productService.findAll();
        model.addAttribute("products",productList);
        model.addAttribute("title", "Главная страница");
        return "main";
    }

}