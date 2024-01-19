package com.example.inventory.inventoryService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.inventoryService.model.Product;

public interface InventoryRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductName(String productName);
}
