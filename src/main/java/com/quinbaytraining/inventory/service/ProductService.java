package com.quinbaytraining.inventory.service;

import com.quinbaytraining.inventory.model.Product;
import com.quinbaytraining.inventory.model.ValidationErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResponseEntity<?> getAllProducts() {
        String sql = "SELECT * FROM products";
        List<Product> products = jdbcTemplate.query(sql, new ProductRowMapper());
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products found.");
        }
        return ResponseEntity.ok(products);
    }

    public Product getProductById(Long id) throws EmptyResultDataAccessException {
        String sql = "SELECT * FROM products WHERE prod_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ProductRowMapper());
    }

    public ResponseEntity<?> addProduct(Product product) {
        if (product.getProdPrice() <= 0) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Product price must be greater than 0", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        if (product.getProdQuantity() < 0) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Product quantity must not be negative", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String sql = "INSERT INTO products (prod_name, prod_price, prod_quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, product.getProdName(), product.getProdPrice(), product.getProdQuantity());
        return ResponseEntity.ok("Product Added");
    }

    public ResponseEntity<?> updateProduct(Long id, Product product) {
//        long id = product.getId();
        String checkSql = "SELECT COUNT(*) FROM products WHERE prod_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        if (count == null || count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found.");
        }
        if (product.getProdPrice() <= 0) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Product price must be greater than 0", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        if (product.getProdQuantity() < 0) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Product quantity must not be negative", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        String sql = "UPDATE products SET prod_name = ?, prod_price = ?, prod_quantity = ? WHERE prod_id = ?";
        jdbcTemplate.update(sql, product.getProdName(), product.getProdPrice(), product.getProdQuantity(), id);
        return ResponseEntity.ok("Product was successfully updated");
    }

    public ResponseEntity<?> deleteProduct(Long id) {
        String checkSql = "SELECT COUNT(*) FROM products WHERE prod_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
        if (count == null || count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID " + id + " not found.");
        }

        String sql = "DELETE FROM products WHERE prod_id = ?";
        jdbcTemplate.update(sql, id);
        return ResponseEntity.ok("Product with ID " + id + " was successfully deleted.");
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getInt("prod_id"));
            product.setProdName(rs.getString("prod_name"));
            product.setProdPrice(rs.getDouble("prod_price"));
            product.setProdQuantity(rs.getInt("prod_quantity"));
            return product;
        }
    }
}
