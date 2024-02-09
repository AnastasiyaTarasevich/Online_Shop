package com.example.kursach.Services;

import com.example.kursach.Model.Category;
import com.example.kursach.Model.Product;
import com.example.kursach.repo.CategoryRepo;
import com.example.kursach.repo.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Data
@AllArgsConstructor
@Service
public class CategoryService
{
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    public void saveCat(int idprod, String name)
    {
       Category category = categoryRepo.findByName(name).orElse(null);
       if (category==null) {
           category = new Category();
           category.setName(name);
//           Product productFromDB = productRepo.findById(idprod).orElse(null);
//           if (productFromDB != null) {
//               category.addProduct(productFromDB);
//           }
           categoryRepo.save(category);
       }
    }

    public Iterable<Category> findAll() {
        return categoryRepo.findAll();
    }
}
