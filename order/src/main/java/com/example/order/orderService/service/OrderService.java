package com.example.order.orderService.service;

import com.example.order.orderService.dto.InventoryCheckRequest;
import com.example.order.orderService.model.Order;
import com.example.order.orderService.model.ProductOrder;
import com.example.order.orderService.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestTemplate restTemplate;

	public boolean areProductsAvailable(Order order) {
		for (ProductOrder entry : order.getProducts()) {
			Long productId = entry.getProductId();
			Integer requiredQuantity = entry.getQuantity();

			InventoryCheckRequest request = new InventoryCheckRequest(productId, requiredQuantity);
			ResponseEntity<Boolean> response = restTemplate.postForEntity("http://INVENTORY/inventory/check", request,
					Boolean.class);
			// If any product is not available in the required quantity, return false
			if (response.getBody() == null || !response.getBody()) {
				return false;
			}
		}
		return true; // All products are available in the required quantity
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<Order> getOrderById(Long id) {
		return orderRepository.findById(id);
	}

	public void saveOrUpdateOrder(Order order) {
		if (areProductsAvailable(order)) {
			orderRepository.save(order);
	    } else {
	    	throw new RuntimeException("One or more products in the order are not available in the required quantity.");
	    }
		
	}

	public void deleteOrder(Long id) {
		getOrderById(id).ifPresent(orderRepository::delete);
	}
}
