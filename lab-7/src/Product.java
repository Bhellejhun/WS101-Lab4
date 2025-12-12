package com.example.lab7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, equals, hashCode, and toString
@AllArgsConstructor // Generates a constructor with all fields
@NoArgsConstructor // Generates a constructor with no arguments
public class Product {

    private Long id;
    private String name;
    private Double price;
}