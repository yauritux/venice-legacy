package com.gdn.venice.logistics.dataexport;

import java.math.BigDecimal;
import java.util.Date;

import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;


public class PickupOrderTemp {
	private String orderId;
	private String wcsOrderId;
	private String orderItemId;
	private String wcsOrderItemId;
	private String gdnRefNo;
	private String wcsProductName;
	private BigDecimal insuranceCost;
	private BigDecimal shippingCost;
	private BigDecimal total;
	private Boolean rmaFlag;
	private Boolean giftWrapFlag;
	private BigDecimal giftWrapPrice;
	private String giftCardNote;
	private Integer cartQuantity;//
	private Integer itemQuantity;
	private Integer dcSequence;//
	private BigDecimal packageWeight;//
	private String serviceCode;
	private String wcsProductSku;
	private String merchantName;
	private String customerName;
	private String recipientName;
	private String merchantPic;
	private String merchantPicPhone;
	private Date pickupDateTime;
	private String ItemspecialHandling;
	private String PickupSpecialHandling;
	private String pickupStreetAddressLine1;
	private String pickupStreetAddressLine2;
	private String pickupKecamatan;
	private String pickupKelurahan;
	private String pickupCity;
	private String pickupState;
	private String pickupPostalCode;
	private String merchantStreetAddressLine1;
	private String merchantStreetAddressLine2;
	private String merchantCity;
	private String merchantState;
	private String airwayBillNumber;
	private boolean isDataBeforeAirwayBillEngine;
	
	private AirwayBillTransaction airwayBillTransaction;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getWcsOrderId() {
		return wcsOrderId;
	}
	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}
	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getWcsOrderItemId() {
		return wcsOrderItemId;
	}
	public void setWcsOrderItemId(String wcsOrderItemId) {
		this.wcsOrderItemId = wcsOrderItemId;
	}
	public String getGdnRefNo() {
		return gdnRefNo;
	}
	public void setGdnRefNo(String gdnRefNo) {
		this.gdnRefNo = gdnRefNo;
	}
	public String getWcsProductName() {
		return wcsProductName;
	}
	public void setWcsProductName(String wcsProductName) {
		this.wcsProductName = wcsProductName;
	}
	public BigDecimal getInsuranceCost() {
		return insuranceCost;
	}
	public void setInsuranceCost(BigDecimal insuranceCost) {
		this.insuranceCost = insuranceCost;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}
	public Boolean getRmaFlag() {
		return rmaFlag;
	}
	public void setRmaFlag(Boolean rmaFlag) {
		this.rmaFlag = rmaFlag;
	}
	public Boolean getGiftWrapFlag() {
		return giftWrapFlag;
	}
	public void setGiftWrapFlag(Boolean giftWrapFlag) {
		this.giftWrapFlag = giftWrapFlag;
	}
	public BigDecimal getGiftWrapPrice() {
		return giftWrapPrice;
	}
	public void setGiftWrapPrice(BigDecimal giftWrapPrice) {
		this.giftWrapPrice = giftWrapPrice;
	}
	public String getGiftCardNote() {
		return giftCardNote;
	}
	public void setGiftCardNote(String giftCardNote) {
		this.giftCardNote = giftCardNote;
	}
	public Integer getCartQuantity() {
		return cartQuantity;
	}
	public void setCartQuantity(Integer cartQuantity) {
		this.cartQuantity = cartQuantity;
	}
	public Integer getItemQuantity() {
		return itemQuantity;
	}
	public void setItemQuantity(Integer itemQuantity) {
		this.itemQuantity = itemQuantity;
	}
	public Integer getDcSequence() {
		return dcSequence;
	}
	public void setDcSequence(Integer dcSequence) {
		this.dcSequence = dcSequence;
	}
	public BigDecimal getPackageWeight() {
		return packageWeight;
	}
	public void setPackageWeight(BigDecimal packageWeight) {
		this.packageWeight = packageWeight;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getWcsProductSku() {
		return wcsProductSku;
	}
	public void setWcsProductSku(String wcsProductSku) {
		this.wcsProductSku = wcsProductSku;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getMerchantPic() {
		return merchantPic;
	}
	public void setMerchantPic(String merchantPic) {
		this.merchantPic = merchantPic;
	}
	public String getMerchantPicPhone() {
		return merchantPicPhone;
	}
	public void setMerchantPicPhone(String merchantPicPhone) {
		this.merchantPicPhone = merchantPicPhone;
	}
	public Date getPickupDateTime() {
		return pickupDateTime;
	}
	public void setPickupDateTime(Date pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}
	public String getItemspecialHandling() {
		return ItemspecialHandling;
	}
	public void setItemspecialHandling(String itemspecialHandling) {
		ItemspecialHandling = itemspecialHandling;
	}
	public String getPickupSpecialHandling() {
		return PickupSpecialHandling;
	}
	public void setPickupSpecialHandling(String pickupSpecialHandling) {
		PickupSpecialHandling = pickupSpecialHandling;
	}
	public String getPickupStreetAddressLine1() {
		return pickupStreetAddressLine1;
	}
	public void setPickupStreetAddressLine1(String pickupStreetAddressLine1) {
		this.pickupStreetAddressLine1 = pickupStreetAddressLine1;
	}
	public String getPickupStreetAddressLine2() {
		return pickupStreetAddressLine2;
	}
	public void setPickupStreetAddressLine2(String pickupStreetAddressLine2) {
		this.pickupStreetAddressLine2 = pickupStreetAddressLine2;
	}
	public String getPickupKecamatan() {
		return pickupKecamatan;
	}
	public void setPickupKecamatan(String pickupKecamatan) {
		this.pickupKecamatan = pickupKecamatan;
	}
	public String getPickupKelurahan() {
		return pickupKelurahan;
	}
	public void setPickupKelurahan(String pickupKelurahan) {
		this.pickupKelurahan = pickupKelurahan;
	}
	public String getPickupCity() {
		return pickupCity;
	}
	public void setPickupCity(String pickupCity) {
		this.pickupCity = pickupCity;
	}
	public String getPickupState() {
		return pickupState;
	}
	public void setPickupState(String pickupState) {
		this.pickupState = pickupState;
	}
	public String getPickupPostalCode() {
		return pickupPostalCode;
	}
	public void setPickupPostalCode(String pickupPostalCode) {
		this.pickupPostalCode = pickupPostalCode;
	}
	public String getMerchantStreetAddressLine1() {
		return merchantStreetAddressLine1;
	}
	public void setMerchantStreetAddressLine1(String merchantStreetAddressLine1) {
		this.merchantStreetAddressLine1 = merchantStreetAddressLine1;
	}
	public String getMerchantStreetAddressLine2() {
		return merchantStreetAddressLine2;
	}
	public void setMerchantStreetAddressLine2(String merchantStreetAddressLine2) {
		this.merchantStreetAddressLine2 = merchantStreetAddressLine2;
	}
	public String getMerchantCity() {
		return merchantCity;
	}
	public void setMerchantCity(String merchantCity) {
		this.merchantCity = merchantCity;
	}
	public String getMerchantState() {
		return merchantState;
	}
	public void setMerchantState(String merchantState) {
		this.merchantState = merchantState;
	}
	public String getAirwayBillNumber() {
		return airwayBillNumber;
	}
	public void setAirwayBillNumber(String airwayBillNumber) {
		this.airwayBillNumber = airwayBillNumber;
	}
	public AirwayBillTransaction getAirwayBillTransaction() {
		return airwayBillTransaction;
	}
	public void setAirwayBillTransaction(AirwayBillTransaction airwayBillTransaction) {
		this.airwayBillTransaction = airwayBillTransaction;
	}
	public boolean isDataBeforeAirwayBillEngine() {
		return isDataBeforeAirwayBillEngine;
	}
	public void setDataBeforeAirwayBillEngine(boolean isDataBeforeAirwayBillEngine) {
		this.isDataBeforeAirwayBillEngine = isDataBeforeAirwayBillEngine;
	}
	
}
