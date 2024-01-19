package com.example.inventory.inventoryService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventory.inventoryService.model.Product;
import com.example.inventory.inventoryService.repository.InventoryRepository;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Product> findAll() {
        return inventoryRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Product save(Product Product) {
        return inventoryRepository.save(Product);
    }

    public Optional<Product> update(Long id, Product Product) {
        if (inventoryRepository.existsById(id)) {
            Product.setId(id);
            return Optional.of(inventoryRepository.save(Product));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }
    
    public boolean isProductAvailable(Long productId, Integer requiredQuantity) {
        Optional<Product> item = inventoryRepository.findById(productId);
        return item.isPresent() && item.get().getQuantity() >= requiredQuantity;
    }

}
