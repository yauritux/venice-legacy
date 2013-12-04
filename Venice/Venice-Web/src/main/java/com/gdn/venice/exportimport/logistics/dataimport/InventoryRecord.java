package com.gdn.venice.exportimport.logistics.dataimport;

import com.gdn.venice.hssf.PojoInterface;

public class InventoryRecord implements PojoInterface {
	
	String productSKU;
	String productName;
	String costOfGoodsSold;
	
	public String getProductSKU() {
		return productSKU;
	}
	public void setProductSKU(String productSKU) {
		this.productSKU = productSKU;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCostOfGoodsSold() {
		return costOfGoodsSold;
	}
	public void setCostOfGoodsSold(String costOfGoodsSold) {
		this.costOfGoodsSold = costOfGoodsSold;
	}
	
}
