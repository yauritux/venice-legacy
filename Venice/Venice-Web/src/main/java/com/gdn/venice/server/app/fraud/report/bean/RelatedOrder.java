package com.gdn.venice.server.app.fraud.report.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RelatedOrder {
	String wcsOrderId;
	String customerName;
	BigDecimal amount;
	Timestamp orderDate;
	Boolean firstTimeFlag;
	String orderStatus;
	String ipAddress;
	
	public String getWcsOrderId() {
		return wcsOrderId;
	}
	
	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public Boolean getFirstTimeFlag() {
		return firstTimeFlag;
	}

	public void setFirstTimeFlag(Boolean firstTimeFlag) {
		this.firstTimeFlag = firstTimeFlag;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}