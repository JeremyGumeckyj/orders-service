package com.service.controller;

import com.service.OrderItemService;
import dto.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/orderItems")
    public List<OrderItem> getAll() {
        return orderItemService.getAll();
    }

    @GetMapping("/orderItems/{id}")
    public OrderItem getById(@PathVariable UUID id) {
        return orderItemService.getById(id);
    }

    @PostMapping("/orderItems")
    public OrderItem addOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemService.addOrderItem(orderItem);
    }

    @PutMapping("/orderItems")
    public OrderItem updateOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemService.updateOrderItem(orderItem);
    }

    @DeleteMapping("/orderItems/{id}")
    public ResponseEntity deleteById(@PathVariable UUID id) {
        orderItemService.deleteById(id);
        return ResponseEntity.ok()
                .build();
    }
}
