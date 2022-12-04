package com.service.controller;

import com.service.OrderItemService;
import dto.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/orderItems")
    public List<OrderItem> getAllOrderItems() {
        return orderItemService.getAllOrderItems();
    }

    @GetMapping("/orderItems/{id}")
    public OrderItem getById(@PathVariable String id){
        return orderItemService.getById(id);
    };

    @PostMapping("/orderItems")
    public OrderItem createOrderItem(@RequestBody OrderItem newOrderItem) {
        return orderItemService.addOrderItem(newOrderItem);
    }
}
