package com.service.impl;

import com.repository.OrderRepository;
import com.service.OrderService;
import com.service.util.exception.NotFoundException;
import dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    public Order createOrder(Order order) {
        order.setId(UUID.randomUUID());
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order) {
        validateIfOrderExists(order.getId());
        return orderRepository.save(order);
    }

    public void deleteById(UUID id) {
        validateIfOrderExists(id);
        orderRepository.deleteById(id);
    }

    private void validateIfOrderExists(UUID id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id in Order entity can not be null");
        }

        Optional<Order> orderFromDB = orderRepository.findById(id);
        orderFromDB
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }
}
