package com.example.kursach.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
public class Shipping {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idshipping", nullable = false)
    private int idshipping;
    @Basic
    @Column(name = "city", nullable = false, length = 255)
    private String city;
    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    private double price;


}
