package com.example.kursach.repo;

import com.example.kursach.Model.Shipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public interface ShippingRepo extends CrudRepository<Shipping,Integer> {
    @Query("SELECT p.price FROM Shipping p WHERE p.city LIKE %?1%")
    double getPriceforCity(String city);



}
