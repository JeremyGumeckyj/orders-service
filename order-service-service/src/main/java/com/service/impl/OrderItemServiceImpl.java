package com.service.impl;

import com.repository.OrderItemRepository;
import com.repository.ProductRepository;
import com.service.OrderItemService;
import com.service.util.exception.NotFoundException;
import dto.OrderItem;
import dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import java.util.ArrayList;


@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    private final List<OrderItem> orderItems = new ArrayList<>();

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public List<OrderItem> getAll() {
        return orderItemRepository.findAll();
    }

    public OrderItem getById(UUID id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order item not found"));
    }

    public OrderItem addOrderItem(OrderItem orderItem) {
        orderItem.setId(UUID.randomUUID());
        Optional<Product> byId = productRepository.findById(orderItem.getProductId());
        if (byId.isPresent()){
            if (orderItem.getQuantity() == 0){
                orderItem.setQuantity(1);
            }
            return orderItemRepository.save(orderItem);
        }
        else{
            throw new IllegalArgumentException("Product doesnt exist");
        }
    }

    public OrderItem updateOrderItem(OrderItem orderItem) {
        validateIfOrderItemExists(orderItem.getId());
        return orderItemRepository.save(orderItem);
    }

    public void deleteById(UUID id) {
        validateIfOrderItemExists(id);
        orderItemRepository.deleteById(id);
    }

    private void validateIfOrderItemExists(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id in OrderItem entity can not be null");
        }

        Optional<OrderItem> orderItemFromDB = orderItemRepository.findById(id);
        orderItemFromDB
                .orElseThrow(() -> new NotFoundException("OrderItem not found"));
    }

}
