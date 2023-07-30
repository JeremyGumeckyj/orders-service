package com.service;

import dto.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getAll();

    Product getById(UUID id);

    Product addProduct(Product product);

    Product updateProduct(Product product);

    void deleteById(UUID id);
}