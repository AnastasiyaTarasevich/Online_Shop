package com.example.kursach.Controllers;

import com.example.kursach.Model.*;
import com.example.kursach.Services.OrderService;
import com.example.kursach.Services.ProductService;
import com.example.kursach.Services.UserService;
import com.example.kursach.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final ShippingRepo shippingRepo;


    private final OrderService orderService;

    private final OrderRepo orderRepo;
    private final CategoryRepo categoryRepo;
    private final ProductService productService;
    @GetMapping("/user_details")
    public String getProfileInfo(Model model, @AuthenticationPrincipal User user)
    {
        model.addAttribute("login",user.getLogin());
        model.addAttribute("email",user.getEmail());
        return "user_details";
    }

    @GetMapping("/updateProfile")
    public String getEdiProfileForm(Model model, @AuthenticationPrincipal User user)
    {
        model.addAttribute("username",user.getLogin());
        model.addAttribute("email",user.getEmail());
        return "/updateProfile";
    }

    @PostMapping("/updateProfile")
    public String updateProfileInfo(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    )
    {
        userService.updateProfile(user, password, email);
        return "redirect:/user/";
    }


    @GetMapping("/catalog")
    public String catalog(  Model model)
    {
        Iterable<Product> products;
        products = productRepo.findAll();
        Iterable<Category> category=categoryRepo.findAll();
        model.addAttribute("categories",category);
        model.addAttribute("products",products);
        return"catalog";
    }


    @PostMapping("/catalog")
    public String filters(@RequestParam(name = "sort", required = false, defaultValue = "price_asc") String sort, Model model)
    {
        List<Product> products=productService.findAll();
        // выполнить необходимые действия для обработки фильтров и сортировки
        Iterable<Category> category=categoryRepo.findAll();
        model.addAttribute("categories",category);
        List<Product> filteredProducts = productService.getFilteredProducts(products,sort);
        // добавить результаты в модель и вернуть представление
        model.addAttribute("products", filteredProducts);
        return "catalog";
    }
    @GetMapping("/catalog{id}")
    public String filterCat(@PathVariable int id, Model model)
    {
        Iterable<Product> products;
        Category category=new Category();
        category.setIdcategory(id);
        products = productRepo.findProductByCategory(category);
        model.addAttribute("products",products);
        Iterable<Category> category1=categoryRepo.findAll();
        model.addAttribute("categories",category1);
        return "catalog";
    }
    @PostMapping("/catalog{id}")
    public String sortCat(@PathVariable int id,@RequestParam(name = "sort", required = false, defaultValue = "price_asc") String sort, Model model)
    {
        Category category=new Category();
        category.setIdcategory(id);
        // выполнить необходимые действия для обработки фильтров и сортировки
        List<Product>  products = productService.findProductByCategory(category);
        Iterable<Category> category1=categoryRepo.findAll();
        model.addAttribute("categories",category1);
        List<Product> filteredProducts = productService.getFilteredProducts(products,sort);
        // добавить результаты в модель и вернуть представление
        model.addAttribute("products", filteredProducts);
        return "catalog";
    }
    @GetMapping("/catalog/search")
    public String productFind(@RequestParam (name="keyword")String key, Model model)
    {
        List<Product> products=productRepo.search(key);
        model.addAttribute("product",products);
        return "catalog";

    }

    @GetMapping("/catalog/{id}")
    public String addToCart(
            @PathVariable int id,
            @AuthenticationPrincipal User userSession
    ) {
        User userFromDB = userRepo.findByLogin(userSession.getUsername());
        Product product=productRepo.findById(id).orElse(null);
        userFromDB.getProductList().add(product);
        userRepo.save(userFromDB);

        return "redirect:/cart";
    }



    @GetMapping("/cart")
    public String getCart(@AuthenticationPrincipal User userSession, Model model) {
        User userFromDB = userRepo.findByLogin(userSession.getUsername());
        model.addAttribute("products", userFromDB.getProductList());

        return "cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(
            @RequestParam(value = "productId") Product product,
            @AuthenticationPrincipal User userSession
    ) {
        User user = userRepo.findByLogin(userSession.getUsername());

        if (product != null) {
            user.getProductList().remove(product);
        }

        userRepo.save(user);

        return "redirect:/cart";
    }
    @GetMapping("/order")
    public String getOrder(@AuthenticationPrincipal User userSession, Model model) {
        User userFromDB = userRepo.findByLogin(userSession.getUsername());
        Iterable<Shipping>shipping=shippingRepo.findAll();
        model.addAttribute("shipping",shipping);
        model.addAttribute("products", userFromDB.getProductList());
        return "order";
    }
    @GetMapping("order/price/{city}")
    @ResponseBody
    public double getPriceForCity(@PathVariable String city) {
        return shippingRepo.getPriceforCity(city);
    }
    @PostMapping("/order")
    public String postOrder(
            @AuthenticationPrincipal User userSession,
            @Valid Order validOrder,
            @RequestParam(name="city") String city,
            BindingResult bindingResult,
            Model model
    ) {
        User user = userRepo.findByLogin(userSession.getUsername());
        Order order = new Order(user);

//        if (bindingResult.hasErrors()) {
//            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
//
//            model.mergeAttributes(errorsMap);
//            model.addAttribute("products", user.getProductList());
//
//            return "order/order";
//        } else {
            order.getProducts().addAll(user.getProductList());
            order.setTotalPrice(validOrder.getTotalPrice());
            order.setFirstName(validOrder.getFirstName());
            order.setLastName(validOrder.getLastName());
            order.setCity(city);
            order.setStreet(validOrder.getStreet());
            order.setNHouse(validOrder.getNHouse());
            order.setCorpus(validOrder.getCorpus());
            order.setNFlat(validOrder.getNFlat());
            order.setPostIndex(validOrder.getPostIndex());
            order.setPhoneNumber(validOrder.getPhoneNumber());
            order.setPriceOfOrder(validOrder.getPriceOfOrder());
            user.getProductList().clear();

            orderService.save(order);
//        }

        return "redirect:/finalizeOrder";
    }
    @GetMapping("/finalizeOrder")
    public String finalizeOrder(Model model) {
        List<Order> orderList = orderService.findAll();
        Order orderIndex = orderList.get(orderList.size() - 1);

        model.addAttribute("orderIndex", orderIndex.getIdorder());

        return "finalizeOrder";
    }
    @GetMapping("/orders")
    public String getUserOrdersList(@AuthenticationPrincipal User userSession, Model model) {
        User userFromDB = userRepo.findByLogin(userSession.getUsername());
        List<Order> orders = orderRepo.findOrderByUser(userFromDB);
        model.addAttribute("orders", orders);

        return "orders";
    }



}

