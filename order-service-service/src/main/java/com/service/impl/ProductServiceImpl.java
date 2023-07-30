package com.service.impl;

import com.repository.ProductRepository;
import com.service.ProductService;
import com.service.util.exception.NotFoundException;
import dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public Product addProduct(Product product) {
        product.setId(UUID.randomUUID());
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        validateIfProductExists(product.getId());
        return productRepository.save(product);
    }

    public void deleteById(UUID id) {
        validateIfProductExists(id);
        productRepository.deleteById(id);
    }

    private void validateIfProductExists(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id in Product entity can not be null");
        }

        Optional<Product> productFromDB = productRepository.findById(id);
        productFromDB
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}