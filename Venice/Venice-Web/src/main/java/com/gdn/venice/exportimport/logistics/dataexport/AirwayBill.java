package com.gdn.venice.exportimport.logistics.dataexport;

import java.util.ArrayList;
import java.util.Date;


public class AirwayBill {
	//airwaybill
	private Date orderDate;	
	private String airwayBillId;
	private String wcsOrderId;
	private String wcsOrderItemId;
	private String orderItemId;
	private String gdnReference;
	private String activityResultStatus;
	private Date airwaybillTimestamp;
	private Date airwayBillPickupDatetime;
	private String airwaybillNumber;
	private String approvalStatus;
	private String approvedBy;
	private String insuredAmount;
	private String orderItemStatus;
	private String mtaSettlement;
	private String orderStatusTimestamp;
	private String destination;
	private String deliveryStatus;
	private Date received;
	private String receiver;	
	private String relation;
	private String logisticService;
	private String merchantName;
	private String merchantOrigin;
	private String productName;
	private String shippingWeight;
	private String quantity;
	private String historyTimestamp;
	private String historyStatus;
	private String historyNotes;
	private String orderOrReturn;
	private String cxDate;
	private String cxFinanceDate;
	private String DDate;
	private String logisticProvider;
	private String originMerchant;
	private String destinationZipCode;
	private ArrayList<ActivityInvoiceReconRecord> activityInvoiceReconRecordList = new ArrayList<ActivityInvoiceReconRecord>();
	
	public String getCxFinanceDate() {
		return cxFinanceDate;
	}
	public void setCxFinanceDate(String cxFinanceDate) {
		this.cxFinanceDate = cxFinanceDate;
	}
	public String getDDate() {
		return DDate;
	}
	public void setDDate(String dDate) {
		DDate = dDate;
	}
	public String getOrderOrReturn() {
		return orderOrReturn;
	}
	public void setOrderOrReturn(String orderOrReturn) {
		this.orderOrReturn = orderOrReturn;
	}
	public String getCxDate() {
		return cxDate;
	}
	public void setCxDate(String cxDate) {
		this.cxDate = cxDate;
	}
	public String getMtaSettlement() {
		return mtaSettlement;
	}
	public void setMtaSettlement(String mtaSettlement) {
		this.mtaSettlement = mtaSettlement;
	}
	public String getMerchantOrigin() {
		return merchantOrigin;
	}
	public void setMerchantOrigin(String merchantOrigin) {
		this.merchantOrigin = merchantOrigin;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getHistoryTimestamp() {
		return historyTimestamp;
	}
	public void setHistoryTimestamp(String historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}
	public String getHistoryStatus() {
		return historyStatus;
	}
	public void setHistoryStatus(String historyStatus) {
		this.historyStatus = historyStatus;
	}
	public String getHistoryNotes() {
		return historyNotes;
	}
	public void setHistoryNotes(String historyNotes) {
		this.historyNotes = historyNotes;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public Date getReceived() {
		return received;
	}
	public void setReceived(Date received) {
		this.received = received;
	}
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getOrderStatusTimestamp() {
		return orderStatusTimestamp;
	}
	public void setOrderStatusTimestamp(String orderStatusTimestamp) {
		this.orderStatusTimestamp = orderStatusTimestamp;
	}
	public String getOrderItemStatus() {
		return orderItemStatus;
	}
	public void setOrderItemStatus(String orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getShippingWeight() {
		return shippingWeight;
	}
	public void setShippingWeight(String shippingWeight) {
		this.shippingWeight = shippingWeight;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getLogisticService() {
		return logisticService;
	}
	public void setLogisticService(String logisticService) {
		this.logisticService = logisticService;
	}
	public String getInsuredAmount() {
		return insuredAmount;
	}
	public void setInsuredAmount(String insuredAmount) {
		this.insuredAmount = insuredAmount;
	}
	public String getAirwayBillId() {
		return airwayBillId;
	}
	public void setAirwayBillId(String airwayBillId) {
		this.airwayBillId = airwayBillId;
	}
	public String getWcsOrderId() {
		return wcsOrderId;
	}
	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}
	public String getWcsOrderItemId() {
		return wcsOrderItemId;
	}
	public void setWcsOrderItemId(String wcsOrderItemId) {
		this.wcsOrderItemId = wcsOrderItemId;
	}
	public String getGdnReference() {
		return gdnReference;
	}
	public void setGdnReference(String gdnReference) {
		this.gdnReference = gdnReference;
	}
	public String getActivityResultStatus() {
		return activityResultStatus;
	}
	public void setActivityResultStatus(String activityResultStatus) {
		this.activityResultStatus = activityResultStatus;
	}
	public Date getAirwaybillTimestamp() {
		return airwaybillTimestamp;
	}
	public void setAirwaybillTimestamp(Date airwaybillTimestamp) {
		this.airwaybillTimestamp = airwaybillTimestamp;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Date getAirwayBillPickupDatetime() {
		return airwayBillPickupDatetime;
	}
	public void setAirwayBillPickupDatetime(Date airwayBillPickupDatetime) {
		this.airwayBillPickupDatetime = airwayBillPickupDatetime;
	}
	public String getAirwaybillNumber() {
		return airwaybillNumber;
	}
	public void setAirwaybillNumber(String airwaybillNumber) {
		this.airwaybillNumber = airwaybillNumber;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public String getLogisticProvider() {
		return logisticProvider;
	}
	public void setLogisticProvider(String logisticProvider) {
		this.logisticProvider = logisticProvider;
	}
	public String getOriginMerchant() {
		return originMerchant;
	}
	public void setOriginMerchant(String originMerchant) {
		this.originMerchant = originMerchant;
	}
	public String getDestinationZipCode() {
		return destinationZipCode;
	}
	public void setDestinationZipCode(String destinationZipCode) {
		this.destinationZipCode = destinationZipCode;
	}
	
	public ArrayList<ActivityInvoiceReconRecord> getActivityInvoiceReconRecordList() {
		return activityInvoiceReconRecordList;
	}
	public void setActivityInvoiceReconRecordList(
			ArrayList<ActivityInvoiceReconRecord> activityInvoiceReconRecordList) {
		this.activityInvoiceReconRecordList = activityInvoiceReconRecordList;
	}
	public void ActivityInvoiceReconRecord(ActivityInvoiceReconRecord activityInvoiceReconRecord) {
		if(this.activityInvoiceReconRecordList == null){
			activityInvoiceReconRecordList = new ArrayList<ActivityInvoiceReconRecord>();
		}
		
		activityInvoiceReconRecordList.add(activityInvoiceReconRecord);
	}
	
	
	
}
