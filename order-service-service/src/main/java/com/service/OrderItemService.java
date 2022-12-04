package com.service;

import dto.OrderItem;
import java.util.List;

public interface OrderItemService {
    List<OrderItem> getAllOrderItems();
    OrderItem addOrderItem(OrderItem orderItem);
    OrderItem getById(String id);
}
