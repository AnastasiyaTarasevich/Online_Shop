package com.example.kursach.Controllers;

import com.example.kursach.Model.User;
import com.example.kursach.Services.UserService;
import com.example.kursach.dto.CaptchaResponseDto;
import com.example.kursach.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class AuthorizController {
    private final UserService userService;
    private final UserRepo userRepo;
    private final RestTemplate restTemplate;
    public static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Value("${recaptcha.secret")
    private String secret;

    @PostMapping("/authoriz")
    public String addAuhorith(Model model, @Valid User user,
                              @RequestParam (name = "confirmPassword")String confirmPassword,
                              @RequestParam("g-recaptcha-response") String captchaResponse
                              )
    {
//        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
//        System.out.println(captchaResponse);
//        CaptchaResponseDto responses = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
//        if (responses.isSuccess() == false) {
//            model.addAttribute("captchaError", "Fill captcha");
//            return "authoriz";
//        }
         if(!confirmPassword.equals(user.getPassword()))
        {
            model.addAttribute("passwError", "Пароли не совпадают");
            return "authoriz";
        }
        else if(!userService.createUser(user)) {
            model.addAttribute("usernameError", "Пользователь существует!");
            return "authoriz";
        }
        else return "redirect:/main";

    }
    @GetMapping("/login")
    public String login( Model model) {
        return "login";
    }
    @GetMapping("/authoriz")
    public String getRegistr(Model model) {
        return "authoriz";
    }
    @GetMapping("/user/")
    public String getUser(Model model, @AuthenticationPrincipal User user)
    {
        model.addAttribute("login",user.getLogin());
        return "user";
    }
    @GetMapping("/admin/")
    public String getAdmin(Model model)
    {
        return "admin";
    }


}
