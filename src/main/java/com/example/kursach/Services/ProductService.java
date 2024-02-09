package com.example.kursach.Services;

import com.example.kursach.Model.Category;
import com.example.kursach.Model.Image;
import com.example.kursach.Model.Product;
import com.example.kursach.repo.CategoryRepo;
import com.example.kursach.repo.ImageRepo;
import com.example.kursach.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepo productRepo;
    private final  ImageRepo imageRepo ;
    private final CategoryRepo categoryRepo;
    public void saveProduct(Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3, String category) throws IOException {
        Image image1;
        Image image2;
        Image image3;
        if(file1.getSize()!=0)
        {
            image1=addImage(file1);
            image1.setIsPreviewImage(true);
            product.addImagetoProduct(image1);
        }
        if(file2.getSize()!=0)
        {
            image2=addImage(file2);
            product.addImagetoProduct(image2);
        }
        if(file3.getSize()!=0)
        {
            image3=addImage(file3);
            product.addImagetoProduct(image3);
        }
        log.info("Saving new product. ",product.getName(),product.getPrice());
        Category categoryFromDB = categoryRepo.findByName(category).orElse(null);
        product.setCategory(categoryFromDB);
        Product product1=productRepo.save(product);
        product1.setPreviewImageId(product1.getImages().get(0).getIdimage());
        productRepo.save(product);
    }

    private Image addImage(MultipartFile file) throws IOException
    {
        Image image =new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        byte[]bytes=  file.getBytes();
        String byt= Base64.getEncoder().encodeToString(bytes);
        image.setBytes(byt);
        return image;

    }
    public List<Product> findAll()
    {
        Iterable<Product> filteredProducts = productRepo.findAll();
        return (List<Product>) filteredProducts;
    }
    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(null);
    }

   public void updateProduct(int id, Product updatedProduct,MultipartFile file1,MultipartFile file2, MultipartFile file3) throws IOException {
        Image image1,image2,image3;
        Product product=getProductById(id);


       if (!product.getName().equals(updatedProduct.getName()))
       {
           product.setName(updatedProduct.getName());
       }
        if (!product.getVendor_code().equals(updatedProduct.getVendor_code()))
        {
            product.setVendor_code(updatedProduct.getVendor_code());
        }
        if (!product.getDescription().equals(updatedProduct.getDescription()))
        {
            product.setDescription(updatedProduct.getDescription());
        }
        if (product.getWeight()!=updatedProduct.getWeight())
        {
            product.setWeight(updatedProduct.getWeight());
        }
        if (product.getPrice()!=updatedProduct.getPrice())
        {
            product.setPrice(updatedProduct.getPrice());
        }
        if (!product.getManufacturer().equals(updatedProduct.getManufacturer()))
        {
            product.setManufacturer(updatedProduct.getManufacturer());
        }
        if(file1!=null&&!file1.isEmpty())
        {
           image1=addImage(file1);
            image1.setIsPreviewImage(true);
            product.addImagetoProduct(image1);

            if(product.getPreviewImageId()!=null)
            {
                imageRepo.deleteById(product.getPreviewImageId());

            }
            product.setPreviewImageId(image1.getIdimage());
        }
        if (file2!=null && !file2.isEmpty())
        {
            image2=addImage(file2);
            product.addImagetoProduct(image2);
            Image image=imageRepo.findImageByNameAndProduct("second_file",product);
            if(image!=null)
            {
                imageRepo.deleteById(image.getIdimage());
            }
        }
       if (file3!=null && !file3.isEmpty())
       {
           image3=addImage(file3);
           product.addImagetoProduct(image3);
           Image image=imageRepo.findImageByNameAndProduct("third_file",product);
           if(image!=null)
           {
               imageRepo.deleteById(image.getIdimage());
           }
       }
            productRepo.save(product);

    }

   public List<Product> searchProducts(String query)
   {
       List<Product> products=productRepo.search(query);
       return products;
   }
    public BigDecimal minProductPrice() {
        return productRepo.minProductPrice();
    }
    public BigDecimal maxProductPrice() {
        return productRepo.maxProductPrice();
    }
    public List<Product> getFilteredProducts(List <Product>products,String sortBy)
    {


        // Применяем сортировку
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "price_asc":
                    products.sort(Comparator.comparingDouble(Product::getPrice));
                    break;
                case "price_desc":
                    products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
                    break;
                case "good_asc":
                    products.sort(Comparator.comparing(Product::getName));
                    break;
                case "good_desc":
                    products.sort(Comparator.comparing(Product::getName).reversed());
                    break;
                default:
                    break;
            }

        }

        return products;
    }

    public List<Product> findProductByCategory(Category category)
    {
        return productRepo.findProductByCategory(category);
    }
}

