package com.service.controller;

import com.service.ProductService;
import dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {



    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public Product getById(@PathVariable String id){
        return productService.getById(id);
    };

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product newProduct) {
        return productService.addProduct(newProduct);
    }
}