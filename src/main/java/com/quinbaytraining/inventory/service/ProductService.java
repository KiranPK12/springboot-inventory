package com.quinbaytraining.inventory.service;
import com.quinbaytraining.inventory.model.Product;
import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product addProduct(Product product);

    Product updateProduct(Product product);

    String deleteProduct(Long id);
}
