package com.quinbaytraining.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private long id;
    private String prodName;
    private double prodPrice;
    private long prodQuantity;
}
