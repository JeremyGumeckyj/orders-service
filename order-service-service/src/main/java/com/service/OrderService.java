package com.service;

import dto.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAll();

    Order getById(UUID id);

    Order createOrder(Order order);

    Order updateOrder(Order order);

    void  deleteById(UUID id);
}
