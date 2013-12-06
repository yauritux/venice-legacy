package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_migs_upload_temporary database table.
 * 
 */
@Entity
@Table(name="ven_migs_upload_temporary")
public class VenMigsUploadTemporary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_migs_upload_temporary")  
	@TableGenerator(name="ven_migs_upload_temporary", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="migs_id", unique=true, nullable=false)
	private Long migsId;

	@Column(name="acquirer_id", length=100)
	private String acquirerId;

	@Column(name="acquirer_response_code", length=100)
	private String acquirerResponseCode;

	@Column(length=50)
	private String action;

	@Column(precision=20, scale=2)
	private BigDecimal amount;

	@Column(name="authorisation_code", length=100)
	private String authorisationCode;

	@Column(name="batch_number", length=100)
	private String batchNumber;

	@Column(name="card_expiry_month", length=100)
	private String cardExpiryMonth;

	@Column(name="card_expiry_year", length=100)
	private String cardExpiryYear;

	@Column(name="card_number", length=100)
	private String cardNumber;

	@Column(name="card_type", length=100)
	private String cardType;

	@Column(length=1000)
	private String comment;

	@Column(length=100)
	private String currency;

	@Column(name="dialect_csc_result_code", length=100)
	private String dialectCscResultCode;

	@Column(name="ecommerce_indicator", length=100)
	private String ecommerceIndicator;

	@Column(name="file_name", length=1000)
	private String fileName;

	@Column(name="merchant_id", length=100)
	private String merchantId;

	@Column(name="merchant_transaction_reference", length=100)
	private String merchantTransactionReference;

	@Column(name="merchant_transaction_source", length=100)
	private String merchantTransactionSource;

	@Column(length=100)
	private String operator;

	@Column(name="order_date")
	private Timestamp orderDate;

	@Column(name="order_id", length=100)
	private String orderId;

	@Column(name="order_reference", length=100)
	private String orderReference;

	@Column(name="problem_description", length=1000)
	private String problemDescription;

	@Column(name="response_code", length=100)
	private String responseCode;

	@Column(length=100)
	private String rrn;

	@Column(name="transaction_date")
	private Timestamp transactionDate;

	@Column(name="transaction_id", length=100)
	private String transactionId;

	@Column(name="transaction_type", length=100)
	private String transactionType;

    public VenMigsUploadTemporary() {
    }

	public Long getMigsId() {
		return this.migsId;
	}

	public void setMigsId(Long migsId) {
		this.migsId = migsId;
	}

	public String getAcquirerId() {
		return this.acquirerId;
	}

	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public String getAcquirerResponseCode() {
		return this.acquirerResponseCode;
	}

	public void setAcquirerResponseCode(String acquirerResponseCode) {
		this.acquirerResponseCode = acquirerResponseCode;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAuthorisationCode() {
		return this.authorisationCode;
	}

	public void setAuthorisationCode(String authorisationCode) {
		this.authorisationCode = authorisationCode;
	}

	public String getBatchNumber() {
		return this.batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getCardExpiryMonth() {
		return this.cardExpiryMonth;
	}

	public void setCardExpiryMonth(String cardExpiryMonth) {
		this.cardExpiryMonth = cardExpiryMonth;
	}

	public String getCardExpiryYear() {
		return this.cardExpiryYear;
	}

	public void setCardExpiryYear(String cardExpiryYear) {
		this.cardExpiryYear = cardExpiryYear;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardType() {
		return this.cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDialectCscResultCode() {
		return this.dialectCscResultCode;
	}

	public void setDialectCscResultCode(String dialectCscResultCode) {
		this.dialectCscResultCode = dialectCscResultCode;
	}

	public String getEcommerceIndicator() {
		return this.ecommerceIndicator;
	}

	public void setEcommerceIndicator(String ecommerceIndicator) {
		this.ecommerceIndicator = ecommerceIndicator;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantTransactionReference() {
		return this.merchantTransactionReference;
	}

	public void setMerchantTransactionReference(String merchantTransactionReference) {
		this.merchantTransactionReference = merchantTransactionReference;
	}

	public String getMerchantTransactionSource() {
		return this.merchantTransactionSource;
	}

	public void setMerchantTransactionSource(String merchantTransactionSource) {
		this.merchantTransactionSource = merchantTransactionSource;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Timestamp getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderReference() {
		return this.orderReference;
	}

	public void setOrderReference(String orderReference) {
		this.orderReference = orderReference;
	}

	public String getProblemDescription() {
		return this.problemDescription;
	}

	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getRrn() {
		return this.rrn;
	}

	public void setRrn(String rrn) {
		this.rrn = rrn;
	}

	public Timestamp getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}