package com.gdn.venice.logistics.dataexport;

public class FailedStatusUpdate {
	private String gdnRef;
	private String airwayBillNoLogistic;
	private String airwayBillNoMTA;
	private String orderItemStatus;
	private String airwayBillStatus;
	
	
	public String getGdnRef() {
		return gdnRef;
	}
	public void setGdnRef(String gdnRef) {
		this.gdnRef = gdnRef;
	}
	public String getAirwayBillNoLogistic() {
		return airwayBillNoLogistic;
	}
	public void setAirwayBillNoLogistic(String airwayBillNoLogistic) {
		this.airwayBillNoLogistic = airwayBillNoLogistic;
	}
	public String getAirwayBillNoMTA() {
		return airwayBillNoMTA;
	}
	public void setAirwayBillNoMTA(String airwayBillNoMTA) {
		this.airwayBillNoMTA = airwayBillNoMTA;
	}
	public String getOrderItemStatus() {
		return orderItemStatus;
	}
	public void setOrderItemStatus(String statusOrder) {
		this.orderItemStatus = statusOrder;
	}
	public String getAirwayBillStatus() {
		return airwayBillStatus;
	}
	public void setAirwayBillStatus(String statusAirwayBill) {
		this.airwayBillStatus = statusAirwayBill;
	}
	
	
}
