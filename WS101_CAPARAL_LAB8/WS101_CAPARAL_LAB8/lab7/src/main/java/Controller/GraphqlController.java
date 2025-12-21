package com.example.lab7.controller;

import com.example.lab7.ProductService;
import com.example.lab7.Product;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class GraphqlController {

    private final ProductService service;

    // ==================== QUERIES ====================

    @QueryMapping
    public List<Product> allProducts() {
        return service.findAllProducts();
    }

    @SubscriptionMapping
    public List<Product> reactiveFetch() {
        return service.findAllProducts();
    }

    @QueryMapping("findProductById")
    public Product findProductById(@Argument Long id) {
        return service.findById(id);
    }

    @QueryMapping
    public Product productById(@Argument Long id) {
        return service.findById(id);
    }

    // ==================== MUTATIONS ====================

    @MutationMapping
    public Product addNew(@Argument Product product) {
        return service.save(product);
    }

    @MutationMapping
    public Product addProduct(
            @Argument String name,
            @Argument double price
    ) {
        return service.addProduct(name, price);
    }

    @MutationMapping(name = "updateProductName")
    public Product updateProductName(
            @Argument Long productId,
            @Argument String newName
    ) {
        return service.updateProductName(productId, newName);
    }

    @MutationMapping
    public String deleteProduct(@Argument @NonNull Long id) {
        return service.deleteProductById(id)
                ? "Product removed"
                : "Failed to remove product";
    }
}
