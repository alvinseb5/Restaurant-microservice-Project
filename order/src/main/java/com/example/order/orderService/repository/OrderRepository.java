package com.example.order.orderService.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.order.orderService.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Additional custom methods if needed
}
