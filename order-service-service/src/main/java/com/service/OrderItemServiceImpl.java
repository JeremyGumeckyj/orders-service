package com.service;

import dto.OrderItem;
import dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private ProductService productService;
    private List<OrderItem> orderItems;

    public OrderItemServiceImpl() {
        orderItems = new ArrayList<>();
        orderItems.add(new OrderItem("1",2,"1"));
    }
    public List<OrderItem> getAllOrderItems() {
        return orderItems;
    }

    public OrderItem addOrderItem(OrderItem orderItem) {
        orderItem.setId(UUID.randomUUID().toString());
        Product byId = productService.getById(orderItem.getProductId());
        if (byId != null){
            orderItems.add(orderItem);
            return orderItem;
        }else{
            throw new IllegalArgumentException("Product doesnt exist");
        }
    }

    @Override
    public OrderItem getById(String id) {
        return orderItems.stream()
                .filter(orderItem -> orderItem.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
