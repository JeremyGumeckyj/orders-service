package com.service.impl;

import com.repository.OrderItemRepository;
import com.repository.ProductRepository;
import com.repository.OrderRepository;
import com.service.OrderItemService;
import com.service.util.exception.IllegalArgumentException;
import com.service.util.exception.NotFoundException;
import dto.OrderItem;
import dto.Product;
import dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
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
        Optional<Order> byOrderId = orderRepository.findById(orderItem.getOrderId());

        if (byId.isPresent()) {
            if (byOrderId.isPresent()){
                if(byId.get().isAvailable()){
                    Integer orderQuantity = orderItem.getQuantity();
                    orderItem.setQuantity(orderQuantity > 0 ? orderQuantity : 1);
                    return orderItemRepository.save(orderItem);
                }
                else {
                    throw new NotFoundException("Product is not in stock");
                }
            }
            throw new IllegalArgumentException("Invalid order");
        }
        throw new IllegalArgumentException("Invalid product");
    }


    public OrderItem updateOrderItem(OrderItem orderItem) {
        validateIfOrderItemExists(orderItem.getId());
        return orderItemRepository.save(orderItem);
    }

    public void deleteById(UUID id) {
        validateIfOrderItemExists(id);
        orderItemRepository.deleteById(id);
    }

    public void validateIfOrderItemExists(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id in OrderItem entity can not be null");
        }

        Optional<OrderItem> orderItemFromDB = orderItemRepository.findById(id);
        orderItemFromDB
                .orElseThrow(() -> new NotFoundException("OrderItem not found"));
    }
}
