package com.example.kursach.repo;

import com.example.kursach.Model.Category;
import com.example.kursach.Model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ProductRepo extends CrudRepository<Product, Integer> {
@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"+"OR p.vendor_code LIKE %?1%")
  List<Product> search(String keyword);

  List <Product> findProductByCategory(Category category);
  @Query(value = "SELECT min(price) FROM Product ")
  BigDecimal minProductPrice();

  @Query(value = "SELECT max(price) FROM Product ")
  BigDecimal maxProductPrice();

}
