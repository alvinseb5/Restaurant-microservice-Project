package com.example.order.orderService.dto;

public class InventoryCheckRequest {
    private Long productId;
    private Integer requiredQuantity;
    
    
	public InventoryCheckRequest(Long productId, Integer requiredQuantity) {
		super();
		this.productId = productId;
		this.requiredQuantity = requiredQuantity;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Integer getRequiredQuantity() {
		return requiredQuantity;
	}
	public void setRequiredQuantity(Integer requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}

    
}
