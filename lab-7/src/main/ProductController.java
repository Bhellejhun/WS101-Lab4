package com.example.lab7.controller;

import com.example.lab7.model.Product;
import com.example.lab7.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Apply the @RestController Stereotype
@RequestMapping("/api/products") // Define the base path
public class ProductController {

    private final ProductService productService;

    // Inject the ProductService using constructor injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. READ ALL Products (GET /api/products) - Status: 200 OK
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    // 2. READ ONE Product (GET /api/products/{id}) - Status: 200 OK or 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        // Use ResponseEntity for explicit status code handling
        return productService.findById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK)) // Found: 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Not Found: 404 Not Found
    }

    // 3. CREATE a new Product (POST /api/products) - Status: 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Set mandatory HTTP status 201
    public Product createProduct(@RequestBody Product newProduct) {
        // Use @RequestBody to receive the JSON data
        return productService.save(newProduct);
    }

    // 4. UPDATE an existing Product (PUT /api/products/{id}) - Status: 200 OK or 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.update(id, updatedProduct)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK)) // Updated: 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Not Found: 404 Not Found
    }

    // 5. DELETE a Product (DELETE /api/products/{id}) - Status: 204 No Content or 404 Not Found
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteById(id);

        if (deleted) {
            // Success: 204 No Content
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Error: 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}