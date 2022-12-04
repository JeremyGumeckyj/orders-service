package com.service;

import dto.Order;
import dto.OrderItem;
import dto.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderServiceImpl implements OrderService{
    private List<Order> orders;

    public OrderServiceImpl() {
        orders = new ArrayList<>();
        orders.add(new Order("1","name 1","1"));
    }
    public List<Order> getAllOrders() {
        return orders;
    }

    @Override
    public Order addOrder(Order order) {
        order.setId(UUID.randomUUID().toString());
        orders.add(order);
        return order;
    }

    @Override
    public Order getById(String id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Order addOrderItem(String id, OrderItem orderItem) {
        Order byId = getById(id);
        if(byId != null){
            byId.getOrderItemIds().add(orderItem.getId());
            return byId;
        }else{ throw new IllegalArgumentException("No order found by ID"); }
    }
}
