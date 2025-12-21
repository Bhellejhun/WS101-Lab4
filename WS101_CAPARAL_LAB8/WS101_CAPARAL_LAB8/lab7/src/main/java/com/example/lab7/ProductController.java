package com.example.lab7;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public Product getProductById(@Argument Long id) {
        return productService.getProductById(id).orElse(null);
    }

    @MutationMapping
    public Product createProduct(@Argument String name, @Argument Double price) {
        Product product = new Product(null, name, price);
        return productService.createProduct(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id,
                                 @Argument String name,
                                 @Argument Double price) {

        Product updated = new Product(id, name, price);
        return productService.updateProduct(id, updated).orElse(null);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        return productService.deleteProduct(id);
    }
}
