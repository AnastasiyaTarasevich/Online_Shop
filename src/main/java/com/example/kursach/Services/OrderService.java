package com.example.kursach.Services;

import com.example.kursach.Model.Order;
import com.example.kursach.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;


    public Order save(Order order) {
        return orderRepo.save(order);
    }
    public List<Order> findAll() {
        return orderRepo.findAll();
    }
}
