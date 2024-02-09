package com.example.kursach.Controllers;

import com.example.kursach.Model.*;
import com.example.kursach.Services.*;
import com.example.kursach.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class AdminController
{
    private final UserService userService;
    private final  ProductRepo productRepo;
    private final ProductService productService;
    private final UserRepo userRepo;
    private final ShippingRepo shippingRepo;
    private final ImageRepo imageRepo;
    private final CategoryRepo categoryRepo;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final ShippingService shippingService;
    @GetMapping("/products")
    public String getPrMenu(Model model)
    {
        return "products";
    }
    @PostMapping ("/add_prod")
    public String getAddPr(@RequestParam("first_file") MultipartFile file1,@RequestParam("second_file") MultipartFile file2,@RequestParam("third_file") MultipartFile file3,
                          Product product,@RequestParam ("cat") String name,@RequestParam("category1") String category, Model model) throws IOException {
        if (name != null && !name.isEmpty() ) {

            category = name;
        }
        categoryService.saveCat(product.getIdproduct(), category);
        productService.saveProduct(product,file1,file2,file3, category);



        return "redirect:/list_prod";
    }

    @GetMapping("/add_prod")
    public String add_pr(Model model)
    {
        List<Category> categories = (List<Category>) categoryService.findAll();
        if (categories.isEmpty()) {
            // Если список категорий пуст, то создаем новую категорию
            Category newCategory = new Category();
            model.addAttribute("category", newCategory);
        } else {
            model.addAttribute("category", categories);
        }
        return"add_prod";
    }


    @GetMapping("/list_prod")
    public String list_pr(@RequestParam(name="name",required = false) String name, Model model)
    {
        Iterable<Product> products=productRepo.findAll();

        model.addAttribute("products",products);
        return"list_prod";
    }

    @GetMapping("/prod_details/{id}")
    public String productInfo(@PathVariable int id, Model model)
    {
        Product product =productService.getProductById(id);
        Iterable<Image> image = imageRepo.findAll();
        model.addAttribute("product",product);
        model.addAttribute("image", image);
        return "prod_details";

    }

    @GetMapping("/prod_edit/{id}")
    public String productMenuEdit(@PathVariable int id,Model model)
    {
        Product product =productService.getProductById(id);
        model.addAttribute("product",product);
        return"prod_edit";
    }

    @PostMapping("/prod_edit/{id}")
    public String productEdit(@PathVariable int id, @ModelAttribute Product updatedProduct,@RequestParam("first_file") MultipartFile file1,@RequestParam("second_file") MultipartFile file2,@RequestParam("third_file") MultipartFile file3) throws IOException {
        productService.updateProduct(id,updatedProduct,file1,file2,file3);
        return"redirect:/prod_details/{id}";
    }

    @PostMapping("/list_prod/{id}")
    public String productDelete(@PathVariable int id,Model model)
    {
     Product product=productRepo.findById(id).orElseThrow();
     product.setCategory(null);
     productRepo.save(product);
     productRepo.delete(product);
     return "redirect:/list_prod";
    }
    @GetMapping("/list_prod/search")
    public String productFind(@RequestParam (name="keyword")String key, Model model)
    {
        List<Product> products=productRepo.search(key);
        model.addAttribute("product",products);
        return "list_prod";

    }

    @GetMapping("/list_users")
    public String listUsers(Model model)
    {
        Iterable<User> users= userRepo.findAll();
        model.addAttribute("users",users);
        return"list_users";
    }

    @GetMapping("/userEdit/{id}")
    public String userEditForm(Model model, @PathVariable int id) {
        User user =userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping("/userEdit")
    public String userSaveEditForm(
            @RequestParam String login,
            @RequestParam Map<String, String> form,
            @RequestParam("Iduser") User user
    ) {
        userService.userSavenewRole(login, form, user);

        return "redirect:/list_users";
    }
    @GetMapping("/list_shipping")
        public String listShipping(Model model)
    {
        Iterable<Shipping>shipping=shippingRepo.findAll();
        model.addAttribute("shipping",shipping);
        return "list_shipping";
    }
    @GetMapping("/add_ship")
    public String add_sh(Model model)
    {
        return "add_ship";
    }
    @PostMapping ("/add_ship")
    public String getAddSh(Shipping shipping) throws IOException {
        shippingRepo.save(shipping);
        return "redirect:/list_shipping";
    }
    @GetMapping("/shipEdit/{id}")
    public String shipEditForm(Model model, @PathVariable int id) {
        Shipping shipping =shippingService.getShippingById(id);
        model.addAttribute("shipping", shipping);
        return "shipEdit";
    }
    @PostMapping("/shipEdit/{id}")
    public String shipEdit( @PathVariable int id,@ModelAttribute Shipping updSh) {
      shippingService.updateShipping(id,updSh);
        return "redirect:/list_shipping";
    }

    @PostMapping("/list_shipping/{id}")
    public String shippingDelete(@PathVariable int id,Model model)
    {
        Shipping shipping =shippingService.getShippingById(id);
       shippingRepo.delete(shipping);
        return "redirect:/list_shipping";
    }

    @PostMapping("/removeAllSh")
    public String AllShipDelete(Model model)
    {
        shippingRepo.deleteAll();
        return "redirect:/list_shipping";
    }

      @GetMapping("/allOrders")
    public String getAllOrdersList(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        return "allOrders";
    }

    @GetMapping("/list_category")
    public String getAllCategories(Model model)
    {
        Iterable <Category> categories=categoryService.findAll();
        model.addAttribute("categories",categories);
        return "list_category";

    }
    @GetMapping ("/list_category/{id}")
    public String categoryDelete(@PathVariable int id,Model model)
    {
        Category category =categoryRepo.findById(id).orElse(null);
        categoryRepo.delete(category);
        return "redirect:/list_category";
    }
}
