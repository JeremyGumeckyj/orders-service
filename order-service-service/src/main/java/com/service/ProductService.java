package com.service;

import dto.Product;
import org.springframework.stereotype.Component;

import java.util.List;


public interface ProductService {

    List<Product> getAllProducts();
}
