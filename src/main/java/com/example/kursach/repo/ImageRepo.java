package com.example.kursach.repo;

import com.example.kursach.Model.Image;
import com.example.kursach.Model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ImageRepo extends CrudRepository<Image,Long>
{
Image findImageByNameAndProduct(String name, Product product);

}
