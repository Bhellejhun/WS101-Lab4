package com.example.lab7.service;

import com.example.lab7.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    // Simulates the in-memory database
    private final List<Product> products = new ArrayList<>();

    // Used to simulate auto-incrementing IDs for new products (Step 3. Create)
    private final AtomicLong nextId = new AtomicLong(4);

    // Initializer method to create 3 mock products (Step 1.3 - inside the class)
    @PostConstruct
    public void initializeData() {
        products.add(new Product(1L, "Laptop Pro", 1299.99));
        products.add(new Product(2L, "Wireless Mouse", 25.00));
        products.add(new Product(3L, "External SSD 1TB", 89.50));
    }

    // CRUD Implementation:

    // READ ALL Products (GET /api/products)
    public List<Product> findAll() {
        return products;
    }

    // READ ONE Product (GET /api/products/{id})
    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // CREATE a new Product (POST /api/products)
    public Product save(Product newProduct) {
        // Simulate auto-incrementing ID (Step 3. Create)
        newProduct.setId(nextId.getAndIncrement());
        products.add(newProduct);
        return newProduct;
    }

    // UPDATE an existing Product (PUT /api/products/{id})
    public Optional<Product> update(Long id, Product updatedProduct) {
        return findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    return existingProduct;
                });
    }

    // DELETE a Product (DELETE /api/products/{id})
    public boolean deleteById(Long id) {
        // List.removeIf returns true if any element was removed
        return products.removeIf(p -> p.getId().equals(id));
    }
}