package com.gdn.venice.logistics.dataimport;

import com.gdn.venice.hssf.PojoInterface;

public class DailyReportRPX implements PojoInterface {

	private String no;
	private String shipDate;
	private String awb;
	private String shipperName;
	private String shipperCompany;
	private String origin;
	private String consigneeName;
	private String consigneeAdress;
	private String consigneeAdress2;
	private String destination;
	private String trackingStatus;
	private String podDate;
	private String podTime;
	private String PodRecipient;
	private String descOfGood;
	private String gdnSeqNumber;
	private String serviceType;
	private String totalPackage;
	private String weight;
	private String pickupType;
	private String serviceBy;
	private String leadTime;
	private String deliveryDays;
	private String note;
	private String remark;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public String getAwb() {
		return awb;
	}

	public void setAwb(String awb) {
		this.awb = awb;
	}

	public String getShipperName() {
		return shipperName;
	}

	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}

	public String getShipperCompany() {
		return shipperCompany;
	}

	public void setShipperCompany(String shipperCompany) {
		this.shipperCompany = shipperCompany;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeAdress() {
		return consigneeAdress;
	}

	public void setConsigneeAdress(String consigneeAdress) {
		this.consigneeAdress = consigneeAdress;
	}

	public String getConsigneeAdress2() {
		return consigneeAdress2;
	}

	public void setConsigneeAdress2(String consigneeAdress2) {
		this.consigneeAdress2 = consigneeAdress2;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTrackingStatus() {
		return trackingStatus;
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	public String getPodDate() {
		return podDate;
	}

	public void setPodDate(String podDate) {
		this.podDate = podDate;
	}

	public String getPodTime() {
		return podTime;
	}

	public void setPodTime(String podTime) {
		this.podTime = podTime;
	}

	public String getPodRecipient() {
		return PodRecipient;
	}

	public void setPodRecipient(String podRecipient) {
		PodRecipient = podRecipient;
	}

	public String getDescOfGood() {
		return descOfGood;
	}

	public void setDescOfGood(String descOfGood) {
		this.descOfGood = descOfGood;
	}

	public String getGdnSeqNumber() {
		return gdnSeqNumber;
	}

	public void setGdnSeqNumber(String gdnSeqNumber) {
		this.gdnSeqNumber = gdnSeqNumber;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(String totalPackage) {
		this.totalPackage = totalPackage;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPickupType() {
		return pickupType;
	}

	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}

	public String getServiceBy() {
		return serviceBy;
	}

	public void setServiceBy(String serviceBy) {
		this.serviceBy = serviceBy;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public String getDeliveryDays() {
		return deliveryDays;
	}

	public void setDeliveryDays(String deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
