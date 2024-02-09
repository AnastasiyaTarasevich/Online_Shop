package com.example.kursach.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="orders")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idorder", nullable = false)
    private int idorder;
    @Basic
    @Column(name = "city", nullable = false, length = 255)
    private String city;
    @Basic
    @Column(name = "street", nullable = false, length = 255)
    private String street;
    @Basic
    @Column(name = "n_house", nullable = false)
    private int nHouse;
    @Basic
    @Column(name = "n_flat", nullable = true)
    private Integer nFlat;
    @Basic
    @Column(name = "corpus", nullable = true)
    private Integer corpus;
    @Basic
    @Column(name = "price_of_order", nullable = false, precision = 0)
    private double priceOfOrder;
    @Basic
    @Column(name = "total_price", nullable = false, precision = 0)
    private double totalPrice;
    @Basic
    @Column(name = "firstName", nullable = false, length = 255)
    private String firstName;
    @Basic
    @Column(name = "lastName", nullable = false, length = 255)
    private String lastName;
    @Basic
    @Column(name = "phoneNumber", nullable = false, length = 255)
    private String phoneNumber;
    @Basic
    @Column(name = "postIndex", nullable = false)
    private int postIndex;
    @Basic
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OrderColumn
    @ManyToMany( fetch = FetchType.EAGER)
    private List<Product> products;

    @ManyToOne
    private User user;

    public Order(User user) {
        this.date = LocalDate.now();
        this.user = user;
        this.products= new ArrayList<>();
    }
}
