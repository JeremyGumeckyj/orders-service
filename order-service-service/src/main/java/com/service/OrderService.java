package com.service;
import dto.Order;
import dto.OrderItem;
import dto.Product;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order addOrder(Order order);
    Order getById(String id);
    Order addOrderItem(String id, OrderItem orderItem);
}
