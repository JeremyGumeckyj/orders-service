package com.service;

import dto.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    public List<Product> getAllProducts() {
        return new ArrayList<>();
    }
}
