package com.example.kursach.repo;

import com.example.kursach.Model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepo extends CrudRepository<Category,Integer>
{

    public Optional<Category> findByName(String name);
}
