package com.service;

import dto.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {
    List<OrderItem> getAll();

    OrderItem getById(UUID id);

    OrderItem addOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(OrderItem orderItem);

    void deleteById(UUID id);
}
