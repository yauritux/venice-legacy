package com.gdn.venice.server.app.fraud.report.bean;

import java.math.BigDecimal;
import java.util.List;

public class OrderHistory{
	String wcsOrderId;
	String orderTimeStamp;

	String customerName;
	String customerAddress1;
	String customerAddress2;
	String customerKecamatanKelurahan;
	String customerCityPostalCode;
	String customerStateCountry;
	List<OrderHistoryContactDetail>  contactDetailList;

	String shippingType;
	String recipientName;
	String recipientAddress1;
	String recipientAddress2;
	String recipientKecamatanKelurahan;
	String recipientCityPostalCode;
	String recipientStateCountry;
	
	List<OrderHistoryOrderItem> orderItemList;
	
	private String cardType;
	private String paymentType;
	private String paymentInfo;
	private String internetBankingId;
	private String issuingBank;
	private BigDecimal amount;
	private String billingAddress1;
	private String billingAddress2;
	private String billingKecamatanKelurahan;
	private String billingCityPostalCode;
	private String billingStateCountry;
	private BigDecimal handlingFee;
	
	List<OrderHistoryRiskPoint> riskPointList;
	Integer totalRiskPoint;
	String fraudDescription;
	String fraudReason;
	String fraudNotes;
	String investigator;
	
	private String blacklistReason;
	
	private List<OrderHistoryAttachment> attachmentList;
		
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

//	public String getOrderStatus() {
//		return orderStatus;
//	}
//
//	public void setOrderStatus(String orderStatus) {
//		this.orderStatus = orderStatus;
//	}

//	public String getAutoOrManualFP() {
//		return autoOrManualFP;
//	}
//
//	public void setAutoOrManualFP(String autoOrManualFP) {
//		this.autoOrManualFP = autoOrManualFP;
//	}

	public String getInvestigator() {
		return investigator;
	}

	public void setInvestigator(String investigator) {
		this.investigator = investigator;
	}

	public String getWcsOrderId() {
		return wcsOrderId;
	}
	
	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}

	public String getOrderTimeStamp() {
		return orderTimeStamp;
	}

	public void setOrderTimeStamp(String orderTimeStamp) {
		this.orderTimeStamp = orderTimeStamp;
	}

//	public String getFraudStatus() {
//		return fraudStatus;
//	}
//
//	public void setFraudStatus(String fraudStatus) {
//		this.fraudStatus = fraudStatus;
//	}

//	public BigDecimal getOrderAmount() {
//		return orderAmount;
//	}
//
//	public void setOrderAmount(BigDecimal orderAmount) {
//		this.orderAmount = orderAmount;
//	}

//	public String getIpAddress() {
//		return ipAddress;
//	}
//
//	public void setIpAddress(String ipAddress) {
//		this.ipAddress = ipAddress;
//	}
//
//	public String getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(String customerId) {
//		this.customerId = customerId;
//	}
//
//	public String getCustomerType() {
//		return customerType;
//	}
//
//	public void setCustomerType(String customerType) {
//		this.customerType = customerType;
//	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress1() {
		return customerAddress1;
	}

	public void setCustomerAddress1(String customerAddress1) {
		this.customerAddress1 = customerAddress1;
	}

	public String getCustomerAddress2() {
		return customerAddress2;
	}

	public void setCustomerAddress2(String customerAddress2) {
		this.customerAddress2 = customerAddress2;
	}

	public String getCustomerKecamatanKelurahan() {
		return customerKecamatanKelurahan;
	}

	public void setCustomerKecamatanKelurahan(String customerKecamatanKelurahan) {
		this.customerKecamatanKelurahan = customerKecamatanKelurahan;
	}

	public String getCustomerCityPostalCode() {
		return customerCityPostalCode;
	}

	public void setCustomerCityPostalCode(String customerCityPostalCode) {
		this.customerCityPostalCode = customerCityPostalCode;
	}

	public String getCustomerStateCountry() {
		return customerStateCountry;
	}

	public void setCustomerStateCountry(String customerStateCountry) {
		this.customerStateCountry = customerStateCountry;
	}

//	public String getCustomerContact() {
//		return customerContact;
//	}
//
//	public void setCustomerContact(String customerContact) {
//		this.customerContact = customerContact;
//	}
//
//	public String getCustomerEmail() {
//		return customerEmail;
//	}
//
//	public void setCustomerEmail(String customerEmail) {
//		this.customerEmail = customerEmail;
//	}

//	public Date getCustomerBirthDate() {
//		return customerBirthDate;
//	}
//
//	public void setCustomerBirthDate(Date customerBirthDate) {
//		this.customerBirthDate = customerBirthDate;
//	}

	public Integer getTotalRiskPoint() {
		return totalRiskPoint;
	}

	public void setTotalRiskPoint(Integer totalRiskPoint) {
		this.totalRiskPoint = totalRiskPoint;
	}

	public List<OrderHistoryOrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderHistoryOrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}

//	public List<OrderHistoryBilling> getPaymentList() {
//		return paymentList;
//	}
//
//	public void setPaymentList(List<OrderHistoryBilling> paymentList) {
//		this.paymentList = paymentList;
//	}

//	public List<Migs> getMigsList() {
//		return migsList;
//	}
//
//	public void setMigsList(List<Migs> migsList) {
//		this.migsList = migsList;
//	}

	public List<OrderHistoryRiskPoint> getRiskPointList() {
		return riskPointList;
	}

	public void setRiskPointList(List<OrderHistoryRiskPoint> riskPointList) {
		this.riskPointList = riskPointList;
	}

	public String getFraudDescription() {
		return fraudDescription;
	}

	public void setFraudDescription(String fraudDescription) {
		this.fraudDescription = fraudDescription;
	}

	public String getFraudReason() {
		return fraudReason;
	}

	public void setFraudReason(String fraudReason) {
		this.fraudReason = fraudReason;
	}

	public String getFraudNotes() {
		return fraudNotes;
	}

	public void setFraudNotes(String fraudNotes) {
		this.fraudNotes = fraudNotes;
	}

//	public List<RelatedOrder> getRelatedOrderList() {
//		return relatedOrderList;
//	}
//
//	public void setRelatedOrderList(List<RelatedOrder> relatedOrderList) {
//		this.relatedOrderList = relatedOrderList;
//	}
	
//	public List<ActionLog> getActionLogList() {
//		return actionLogList;
//	}
//
//	public void setActionLogList(List<ActionLog> actionLogList) {
//		this.actionLogList = actionLogList;
//	}

	public void setContactDetailList(List<OrderHistoryContactDetail> contactDetailList) {
		this.contactDetailList = contactDetailList;
	}

	public List<OrderHistoryContactDetail> getContactDetailList() {
		return contactDetailList;
	}

//	public void setBilling(List<OrderHistoryBilling> billing) {
//		this.billing = billing;
//	}
//
//	public List<OrderHistoryBilling> getBilling() {
//		return billing;
//	}

	public void setBlacklistReason(String blacklistReason) {
		this.blacklistReason = blacklistReason;
	}

	public String getBlacklistReason() {
		return blacklistReason;
	}

	public void setAttachmentList(List<OrderHistoryAttachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<OrderHistoryAttachment> getAttachmentList() {
		return attachmentList;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardType() {
		return cardType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setInternetBankingId(String internetBankingId) {
		this.internetBankingId = internetBankingId;
	}

	public String getInternetBankingId() {
		return internetBankingId;
	}

	public void setIssuingBank(String issuingBank) {
		this.issuingBank = issuingBank;
	}

	public String getIssuingBank() {
		return issuingBank;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingKecamatanKelurahan(String billingKecamatanKelurahan) {
		this.billingKecamatanKelurahan = billingKecamatanKelurahan;
	}

	public String getBillingKecamatanKelurahan() {
		return billingKecamatanKelurahan;
	}

	public void setBillingCityPostalCode(String billingCityPostalCode) {
		this.billingCityPostalCode = billingCityPostalCode;
	}

	public String getBillingCityPostalCode() {
		return billingCityPostalCode;
	}

	public void setBillingStateCountry(String billingStateCountry) {
		this.billingStateCountry = billingStateCountry;
	}

	public String getBillingStateCountry() {
		return billingStateCountry;
	}

	public void setHandlingFee(BigDecimal handlingFee) {
		this.handlingFee = handlingFee;
	}

	public BigDecimal getHandlingFee() {
		return handlingFee;
	}
}
