package com.gdn.venice.server.app.fraud.report.bean;

import java.math.BigDecimal;

public class Payment {
	String wcsPaymentId;
	String paymentType;
	String paymentInfo;
	String cardType;
	String cardDescription;
	BigDecimal cardLimit;
	Integer tenor;
	BigDecimal installment;
	Float interest;
	BigDecimal amount;
	String billingAddress1;
	String billingAddress2;
	String billingKecamatanKelurahan;
	String billingCityPostalCode;
	String billingStateCountry;
	String internetBankingId;
	String eci;
	String issuingBank;

	public String getIssuingBank() {
		return issuingBank;
	}

	public void setIssuingBank(String issuingBank) {
		this.issuingBank = issuingBank;
	}

	public String getCardDescription() {
		return cardDescription;
	}

	public void setCardDescription(String cardDescription) {
		this.cardDescription = cardDescription;
	}

	public String getEci() {
		return eci;
	}

	public void setEci(String eci) {
		this.eci = eci;
	}

	public String getInternetBankingId() {
		return internetBankingId;
	}

	public void setInternetBankingId(String internetBankingId) {
		this.internetBankingId = internetBankingId;
	}

	public String getWcsPaymentId() {
		return wcsPaymentId;
	}
	
	public void setWcsPaymentId(String wcsPaymentId) {
		this.wcsPaymentId = wcsPaymentId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public BigDecimal getCardLimit() {
		return cardLimit;
	}

	public void setCardLimit(BigDecimal cardLimit) {
		this.cardLimit = cardLimit;
	}

	public Integer getTenor() {
		return tenor;
	}

	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}

	public BigDecimal getInstallment() {
		return installment;
	}

	public void setInstallment(BigDecimal installment) {
		this.installment = installment;
	}

	public Float getInterest() {
		return interest;
	}

	public void setInterest(Float interest) {
		this.interest = interest;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingKecamatanKelurahan() {
		return billingKecamatanKelurahan;
	}

	public void setBillingKecamatanKelurahan(String billingKecamatanKelurahan) {
		this.billingKecamatanKelurahan = billingKecamatanKelurahan;
	}

	public String getBillingCityPostalCode() {
		return billingCityPostalCode;
	}

	public void setBillingCityPostalCode(String billingCityPostalCode) {
		this.billingCityPostalCode = billingCityPostalCode;
	}

	public String getBillingStateCountry() {
		return billingStateCountry;
	}

	public void setBillingStateCountry(String billingStateCountry) {
		this.billingStateCountry = billingStateCountry;
	}
}
