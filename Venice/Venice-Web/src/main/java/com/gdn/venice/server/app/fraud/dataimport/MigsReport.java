package com.gdn.venice.server.app.fraud.dataimport;

import java.math.BigDecimal;
import java.util.Date;

public class MigsReport implements PojoInterface {
	private String transactionId;
	private Date date;
	private String merchantId;
	private String orderReference;
	private String orderId;
	private String merchantTransactionReference;
	private String transactionType;
	private String acquirerId;
	private String batchNumber;
	private String currency;
	private BigDecimal amount;
	private String rrn;
	private String responseCode;
	private String acquirerResponseCode;
	private String authorisationCode;
	private String operatorId;
	private String merchantTransactionSource;
	private Date orderDate;
	private String cardType;
	private String cardNumber;
	private String cardExpiryMonth;
	private String cardExpiryYear;
	private String dialectCSCResultCode;
	private String comment;
	private String eCommerceIndicator;
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getOrderReference() {
		return orderReference;
	}

	public void setOrderReference(String orderReference) {
		this.orderReference = orderReference;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMerchantTransactionReference() {
		return merchantTransactionReference;
	}

	public void setMerchantTransactionReference(String merchantTransactionReference) {
		this.merchantTransactionReference = merchantTransactionReference;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getAcquirerId() {
		return acquirerId;
	}

	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getRrn() {
		return rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getAcquirerResponseCode() {
		return acquirerResponseCode;
	}

	public void setAcquirerResponseCode(String acquirerResponseCode) {
		this.acquirerResponseCode = acquirerResponseCode;
	}

	public String getAuthorisationCode() {
		return authorisationCode;
	}

	public void setAuthorisationCode(String authorisationCode) {
		this.authorisationCode = authorisationCode;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getMerchantTransactionSource() {
		return merchantTransactionSource;
	}

	public void setMerchantTransactionSource(String merchantTransactionSource) {
		this.merchantTransactionSource = merchantTransactionSource;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardExpiryMonth() {
		return cardExpiryMonth;
	}

	public void setCardExpiryMonth(String cardExpiryMonth) {
		this.cardExpiryMonth = cardExpiryMonth;
	}

	public String getCardExpiryYear() {
		return cardExpiryYear;
	}

	public void setCardExpiryYear(String cardExpiryYear) {
		this.cardExpiryYear = cardExpiryYear;
	}

	public String getDialectCSCResultCode() {
		return dialectCSCResultCode;
	}

	public void setDialectCSCResultCode(String dialectCSCResultCode) {
		this.dialectCSCResultCode = dialectCSCResultCode;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String geteCommerceIndicator() {
		return eCommerceIndicator;
	}

	public void seteCommerceIndicator(String eCommerceIndicator) {
		this.eCommerceIndicator = eCommerceIndicator;
	}
}
