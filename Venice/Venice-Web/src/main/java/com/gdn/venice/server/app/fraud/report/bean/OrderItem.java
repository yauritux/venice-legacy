package com.gdn.venice.server.app.fraud.report.bean;

import java.math.BigDecimal;

public class OrderItem {
	String orderItemId;
	String productSku;
	String productName;
	String productCategory1;
	String productCategory2;
	String productCategory3;
	Integer quantity;
	BigDecimal price;
	BigDecimal totalPrice;
	String shippingType;
	String recipientName;
	String recipientAddress1;
	String recipientAddress2;
	String recipientKecamatanKelurahan;
	String recipientCityPostalCode;
	String recipientStateCountry;
	
	public String getProductCategory1() {
		return productCategory1;
	}

	public void setProductCategory1(String productCategory1) {
		this.productCategory1 = productCategory1;
	}

	public String getProductCategory2() {
		return productCategory2;
	}

	public void setProductCategory2(String productCategory2) {
		this.productCategory2 = productCategory2;
	}

	public String getProductCategory3() {
		return productCategory3;
	}

	public void setProductCategory3(String productCategory3) {
		this.productCategory3 = productCategory3;
	}

	public String getOrderItemId() {
		return orderItemId;
	}
	
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientAddress1() {
		return recipientAddress1;
	}

	public void setRecipientAddress1(String recipientAddress1) {
		this.recipientAddress1 = recipientAddress1;
	}

	public String getRecipientAddress2() {
		return recipientAddress2;
	}

	public void setRecipientAddress2(String recipientAddress2) {
		this.recipientAddress2 = recipientAddress2;
	}

	public String getRecipientKecamatanKelurahan() {
		return recipientKecamatanKelurahan;
	}

	public void setRecipientKecamatanKelurahan(String recipientKecamatanKelurahan) {
		this.recipientKecamatanKelurahan = recipientKecamatanKelurahan;
	}

	public String getRecipientCityPostalCode() {
		return recipientCityPostalCode;
	}

	public void setRecipientCityPostalCode(String recipientCityPostalCode) {
		this.recipientCityPostalCode = recipientCityPostalCode;
	}

	public String getRecipientStateCountry() {
		return recipientStateCountry;
	}

	public void setRecipientStateCountry(String recipientStateCountry) {
		this.recipientStateCountry = recipientStateCountry;
	}
}