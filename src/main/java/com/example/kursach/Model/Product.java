package com.example.kursach.Model;

import com.example.kursach.repo.ImageRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idproduct", nullable = false)
    private int idproduct;
    @Basic
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Basic
    @Column(name = "vendor_code", nullable = false)
    private String vendor_code;
    @Basic
    @Column(name = "description", nullable = false, length = 255)
    private String description;
    @Basic
    @Column(name = "weight", nullable = false, precision = 0)
    private double weight;
    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    private double price;

    @Basic
    @Column(name = "manufacturer")
    private String manufacturer;



    @PrePersist
    private void init()
    {
        dateOfCreated = LocalDateTime.now();
    }
    public void addImagetoProduct(Image image)
    {
        image.setProduct(this);
        images.add(image);
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> images = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Category category;
    private Long previewImageId;
    private LocalDateTime dateOfCreated;



}
