package com.example.inventory.inventoryService.controller;

import com.example.inventory.inventoryService.dto.InventoryCheckRequest;
import com.example.inventory.inventoryService.model.Product;
import com.example.inventory.inventoryService.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // GET endpoint to retrieve all inventory items
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> items = inventoryService.findAll();
        return ResponseEntity.ok(items);
    }

    // GET endpoint to retrieve a single inventory item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> item = inventoryService.findById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST endpoint to add a new inventory item
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product Product) {
        Product newItem = inventoryService.save(Product);
        return ResponseEntity.ok(newItem);
    }

    // PUT endpoint to update an existing inventory item
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product Product) {
        Optional<Product> updatedItem = inventoryService.update(id, Product);
        return updatedItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping("/check")
    public ResponseEntity<Boolean> checkProductAvailability(@RequestBody InventoryCheckRequest request) {
        boolean isAvailable = inventoryService.isProductAvailable(request.getProductId(), request.getRequiredQuantity());
        return ResponseEntity.ok(isAvailable);
    }

    // Additional endpoints as required...
}
