package com.service;

import dto.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductServiceImpl implements ProductService {

    private List<Product> products;

    public ProductServiceImpl() {
        products = new ArrayList<>();
        products.add(new Product("1","name 1", 10, false));
    }

    public List<Product> getAllProducts() {
        return products;
    }
    public Product addProduct(Product product){
        product.setId(UUID.randomUUID().toString());
        products.add(product);
        return product;
    }
    public Product getById(String id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}