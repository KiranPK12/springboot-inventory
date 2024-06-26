package com.quinbaytraining.inventory.repository;

import com.quinbaytraining.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
