package com.service.controller;

import com.service.OrderService;
import dto.Order;
import dto.OrderItem;
import dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {



    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getById(@PathVariable String id){
        return orderService.getById(id);
    };

    @PostMapping("/orders")
    public Order createProduct(@RequestBody Order newOrder) {
        return orderService.addOrder(newOrder);
    }

    @PutMapping("/orders/{id}/addItem")
    public Order addOrderItem(@PathVariable String id, @RequestBody OrderItem orderItem) {
        return orderService.addOrderItem(id,orderItem);
    }
}
