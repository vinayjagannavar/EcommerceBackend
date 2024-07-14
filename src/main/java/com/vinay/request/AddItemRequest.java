package com.vinay.request;

public class AddItemRequest {
	private Long ProductId;
	private String size;
	private int quantity;
	private Integer price;
	
	public AddItemRequest() {
		
	}

	public AddItemRequest(Long productId, String size, int quantity, Integer price) {
		super();
		ProductId = productId;
		this.size = size;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getProductId() {
		return ProductId;
	}

	public void setProductId(Long productId) {
		ProductId = productId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}
	
	
}
