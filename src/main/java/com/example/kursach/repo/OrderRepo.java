package com.example.kursach.repo;


import com.example.kursach.Model.Order;
import com.example.kursach.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepo extends CrudRepository<Order,Integer> {
    Order save(Order order);
    List<Order> findAll();
    List<Order> findOrderByUser(User user);
}
