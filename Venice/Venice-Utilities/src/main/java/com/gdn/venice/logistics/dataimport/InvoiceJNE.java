package com.gdn.venice.logistics.dataimport;

import com.gdn.venice.hssf.PojoInterface;

public class InvoiceJNE implements PojoInterface {
	private String no;
	private String date;
	private String awbNumber;
	private String origin;
	private String gdnRef;
	private String destination;
	private String receiver;
	private String qty;
	private String weight;
	private String pricePerKg;
	private String amount;
	private String insurance;
	private String insMin5000;
	private String addCharge;
	private String totalAmount;
	private String type;
	private String service;
	private String podDate;
	private String podCode;
	private String podReceiver;
	private String keterangan;
	private String hasilPengecekan;
	
	public String getNo() {
		return no;
	}
	
	public void setNo(String no) {
		this.no = no;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getAwbNumber() {
		return awbNumber;
	}
	
	public void setAwbNumber(String awbNumber) {
		this.awbNumber = awbNumber;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public String getGdnRef() {
		return gdnRef;
	}
	
	public void setGdnRef(String gdnRef) {
		this.gdnRef = gdnRef;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public String getReceiver() {
		return receiver;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	public String getQty() {
		return qty;
	}
	
	public void setQty(String qty) {
		this.qty = qty;
	}
	
	public String getWeight() {
		return weight;
	}
	
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	public String getPricePerKg() {
		return pricePerKg;
	}
	
	public void setPricePerKg(String pricePerKg) {
		this.pricePerKg = pricePerKg;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getInsurance() {
		return insurance;
	}
	
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	
	public String getInsMin5000() {
		return insMin5000;
	}
	
	public void setInsMin5000(String insMin5000) {
		this.insMin5000 = insMin5000;
	}
	
	public String getAddCharge() {
		return addCharge;
	}
	
	public void setAddCharge(String addCharge) {
		this.addCharge = addCharge;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getService() {
		return service;
	}
	
	public void setService(String service) {
		this.service = service;
	}
	
	public String getPodDate() {
		return podDate;
	}
	
	public void setPodDate(String podDate) {
		this.podDate = podDate;
	}
	
	public String getPodCode() {
		return podCode;
	}
	
	public void setPodCode(String podCode) {
		this.podCode = podCode;
	}
	
	public String getPodReceiver() {
		return podReceiver;
	}
	
	public void setPodReceiver(String podReceiver) {
		this.podReceiver = podReceiver;
	}
	
	public String getKeterangan() {
		return keterangan;
	}
	
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	
	public String getHasilPengecekan() {
		return hasilPengecekan;
	}
	
	public void setHasilPengecekan(String hasilPengecekan) {
		this.hasilPengecekan = hasilPengecekan;
	}	
}
