package com.service.impl;

import com.repository.OrderRepository;
import com.repository.UserRepository;
import com.service.OrderService;
import com.service.util.exception.NotFoundException;
import dto.Order;
import dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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
        Optional<User> byId = userRepository.findById(order.getUserId());
        if(byId.isPresent()){
            return orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("This user is not registered");
        }
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
