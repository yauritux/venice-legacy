package com.gdn.venice.logistics.dataexport;

import java.util.Date;

public class PickupOrder {
	private String logisticProviderName;
	private Date pickupDateTime;
	private String picName;
	private String picPhone;
	private String pickupPointAddressLine1;
	private String pickupPointAddressLine2;
	private String pickupPointKecamatan;
	private String pickupPointKelurahan;
	private String pickupPointCity;
	private String pickupPointProvince;
	private String pickupPointZipCode;
	private String merchantName;
	private String merchantAddressLine1;
	private String merchantAddressLine2;
	private String merchantCity;
	private String merchantProvince;
	private String merchantPhone;
	private String orderOrRMA;
	private String orderId;
	private String orderItemId;
//	private String distributionCartSeq;
	private String skuId;
	private String description;
	private Integer quantity;
	private Boolean isInsured;
	private Double insuredAmount;
	private String serviceType;
	private String giftWrap;
	private String giftCard;
	private String specialHandlingInstruction;
	private String pickupMethod;
	private String customerName;
	private String recipientName;
	private String recipientAddress;
	private String recipientKecamatan;
	private String recipientKelurahan;
	private String recipientCity;
	private String recipientProvince;
	private String recipientZipCode;
	private String recipientPhone;
	private String recipientMobile;
	private String senderEmail;
	private String senderPhone;
	private String senderMobile;
	private String airwayBillNo;
	private String gdnRefNo;
	private boolean isDataBeforeAirwayBillEngine;
	
	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getSenderMobile() {
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}

	public String getPickupMethod() {
		return pickupMethod;
	}

	public void setPickupMethod(String pickupMethod) {
		this.pickupMethod = pickupMethod;
	}

	public String getLogisticProviderName() {
		return logisticProviderName;
	}
	
	public void setLogisticProviderName(String logisticProviderName) {
		this.logisticProviderName = logisticProviderName;
	}
	
	public Date getPickupDateTime() {
		return pickupDateTime;
	}
	
	public void setPickupDateTime(Date pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}
	
	public String getPicName() {
		return picName;
	}
	
	public void setPicName(String picName) {
		this.picName = picName;
	}
	
	public String getPicPhone() {
		return picPhone;
	}
	
	public void setPicPhone(String picPhone) {
		this.picPhone = picPhone;
	}
	
	public String getPickupPointAddressLine1() {
		return pickupPointAddressLine1;
	}
	
	public void setPickupPointAddressLine1(String pickupPointAddressLine1) {
		this.pickupPointAddressLine1 = pickupPointAddressLine1;
	}
	
	public String getPickupPointAddressLine2() {
		return pickupPointAddressLine2;
	}
	
	public void setPickupPointAddressLine2(String pickupPointAddressLine2) {
		this.pickupPointAddressLine2 = pickupPointAddressLine2;
	}
	
	public String getPickupPointKecamatan() {
		return pickupPointKecamatan;
	}
	
	public void setPickupPointKecamatan(String pickupPointKecamatan) {
		this.pickupPointKecamatan = pickupPointKecamatan;
	}
	
	public String getPickupPointKelurahan() {
		return pickupPointKelurahan;
	}
	
	public void setPickupPointKelurahan(String pickupPointKelurahan) {
		this.pickupPointKelurahan = pickupPointKelurahan;
	}
	
	public String getPickupPointCity() {
		return pickupPointCity;
	}
	
	public void setPickupPointCity(String pickupPointCity) {
		this.pickupPointCity = pickupPointCity;
	}
	
	public String getPickupPointProvince() {
		return pickupPointProvince;
	}
	
	public void setPickupPointProvince(String pickupPointProvince) {
		this.pickupPointProvince = pickupPointProvince;
	}
	
	public String getPickupPointZipCode() {
		return pickupPointZipCode;
	}
	
	public void setPickupPointZipCode(String pickupPointZipCode) {
		this.pickupPointZipCode = pickupPointZipCode;
	}
	
	public String getMerchantName() {
		return merchantName;
	}
	
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public String getMerchantAddressLine1() {
		return merchantAddressLine1;
	}
	
	public void setMerchantAddressLine1(String merchantAddressLine1) {
		this.merchantAddressLine1 = merchantAddressLine1;
	}
	
	public String getMerchantAddressLine2() {
		return merchantAddressLine2;
	}
	
	public void setMerchantAddressLine2(String merchantAddressLine2) {
		this.merchantAddressLine2 = merchantAddressLine2;
	}
	
	public String getMerchantCity() {
		return merchantCity;
	}
	
	public void setMerchantCity(String merchantCity) {
		this.merchantCity = merchantCity;
	}
	
	public String getMerchantProvince() {
		return merchantProvince;
	}
	
	public void setMerchantProvince(String merchantProvince) {
		this.merchantProvince = merchantProvince;
	}
	
	public String getMerchantPhone() {
		return merchantPhone;
	}
	
	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}
	
	public String getOrderId() {
		return orderId;
	}
	
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderItemId() {
		return orderItemId;
	}
	
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	
//	public String getDistributionCartSeq() {
//		return distributionCartSeq;
//	}
//	
//	public void setDistributionCartSeq(String distributionCartSeq) {
//		this.distributionCartSeq = distributionCartSeq;
//	}
	
	public String getSkuId() {
		return skuId;
	}
	
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Boolean getIsInsured() {
		return isInsured;
	}
	
	public void setIsInsured(Boolean isInsured) {
		this.isInsured = isInsured;
	}
	
	public Double getInsuredAmount() {
		return insuredAmount;
	}
	
	public void setInsuredAmount(Double insuredAmount) {
		this.insuredAmount = insuredAmount;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getGiftWrap() {
		return giftWrap;
	}
	
	public void setGiftWrap(String giftWrap) {
		this.giftWrap = giftWrap;
	}
	
	public String getGiftCard() {
		return giftCard;
	}
	
	public void setGiftCard(String giftCard) {
		this.giftCard = giftCard;
	}
	
	public String getSpecialHandlingInstruction() {
		return specialHandlingInstruction;
	}
	
	public void setSpecialHandlingInstruction(String specialHandlingInstruction) {
		this.specialHandlingInstruction = specialHandlingInstruction;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getRecipientName() {
		return recipientName;
	}
	
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	
	public String getRecipientAddress() {
		return recipientAddress;
	}
	
	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}
	
	public String getRecipientKecamatan() {
		return recipientKecamatan;
	}
	
	public void setRecipientKecamatan(String recipientKecamatan) {
		this.recipientKecamatan = recipientKecamatan;
	}
	
	public String getRecipientKelurahan() {
		return recipientKelurahan;
	}
	
	public void setRecipientKelurahan(String recipientKelurahan) {
		this.recipientKelurahan = recipientKelurahan;
	}
	
	public String getRecipientCity() {
		return recipientCity;
	}
	
	public void setRecipientCity(String recipientCity) {
		this.recipientCity = recipientCity;
	}
	
	public String getRecipientProvince() {
		return recipientProvince;
	}
	
	public void setRecipientProvince(String recipientProvince) {
		this.recipientProvince = recipientProvince;
	}
	
	public String getRecipientZipCode() {
		return recipientZipCode;
	}
	
	public void setRecipientZipCode(String recipientZipCode) {
		this.recipientZipCode = recipientZipCode;
	}
	
	public String getRecipientPhone() {
		return recipientPhone;
	}
	
	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}
	
	public String getRecipientMobile() {
		return recipientMobile;
	}
	
	public void setRecipientMobile(String recipientMobile) {
		this.recipientMobile = recipientMobile;
	}

	/**
	 * @return the orderOrRMA
	 */
	public String getOrderOrRMA() {
		return orderOrRMA;
	}

	/**
	 * @param orderOrRMA the orderOrRMA to set
	 */
	public void setOrderOrRMA(String orderOrRMA) {
		this.orderOrRMA = orderOrRMA;
	}

	public String getAirwayBillNo() {
		return airwayBillNo;
	}

	public void setAirwayBillNo(String airwayBillNo) {
		this.airwayBillNo = airwayBillNo;
	}

	public String getGdnRefNo() {
		return gdnRefNo;
	}

	public void setGdnRefNo(String gdnRefNo) {
		this.gdnRefNo = gdnRefNo;
	}

	public boolean isDataBeforeAirwayBillEngine() {
		return isDataBeforeAirwayBillEngine;
	}

	public void setDataBeforeAirwayBillEngine(boolean isDataBeforeAirwayBillEngine) {
		this.isDataBeforeAirwayBillEngine = isDataBeforeAirwayBillEngine;
	}
	
}
