package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_order_payment_history database table.
 * 
 */
@Entity
@Table(name="ven_order_payment_history")
public class VenOrderPaymentHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_payment_history")  
	@TableGenerator(name="ven_order_payment_history", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_payment_history_id", unique=true, nullable=false)
	private Long orderPaymentHistoryId;

	@Column(nullable=false, precision=20, scale=2)
	private BigDecimal amount;

	@Column(name="bank_id", nullable=false)
	private Long bankId;

	@Column(name="billing_address_id", nullable=false)
	private Long billingAddressId;

	@Column(name="card_holder_name", length=100)
	private String cardHolderName;

	@Column(name="handling_fee", nullable=false, precision=20, scale=2)
	private BigDecimal handlingFee;

	@Column(name="history_timestamp", nullable=false)
	private Timestamp historyTimestamp;

	@Column(name="masked_credit_card_number", length=100)
	private String maskedCreditCardNumber;

	@Column(name="order_payment_id", nullable=false)
	private Long orderPaymentId;

	@Column(name="payment_code", nullable=false, length=100)
	private String paymentCode;

	@Column(name="payment_confirmation_number", length=100)
	private String paymentConfirmationNumber;

	@Column(name="payment_installment_form_flag", nullable=false)
	private Boolean paymentInstallmentFormFlag;

	@Column(name="payment_status_id", nullable=false)
	private Long paymentStatusId;

	@Column(name="payment_timestamp", nullable=false)
	private Timestamp paymentTimestamp;

	@Column(name="payment_type_id", nullable=false)
	private Long paymentTypeId;

	@Column(name="three_ds_security_auth_level", length=1)
	private String threeDsSecurityAuthLevel;

	@Column(name="virtual_account_number", length=100)
	private String virtualAccountNumber;

	@Column(name="wcs_payment_id", nullable=false, length=100)
	private String wcsPaymentId;

	//bi-directional many-to-one association to VenMasterChangeType
    @ManyToOne
	@JoinColumn(name="master_change_id", nullable=false)
	private VenMasterChangeType venMasterChangeType;

    public VenOrderPaymentHistory() {
    }

	public Long getOrderPaymentHistoryId() {
		return this.orderPaymentHistoryId;
	}

	public void setOrderPaymentHistoryId(Long orderPaymentHistoryId) {
		this.orderPaymentHistoryId = orderPaymentHistoryId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getBankId() {
		return this.bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getBillingAddressId() {
		return this.billingAddressId;
	}

	public void setBillingAddressId(Long billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	public String getCardHolderName() {
		return this.cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public BigDecimal getHandlingFee() {
		return this.handlingFee;
	}

	public void setHandlingFee(BigDecimal handlingFee) {
		this.handlingFee = handlingFee;
	}

	public Timestamp getHistoryTimestamp() {
		return this.historyTimestamp;
	}

	public void setHistoryTimestamp(Timestamp historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}

	public String getMaskedCreditCardNumber() {
		return this.maskedCreditCardNumber;
	}

	public void setMaskedCreditCardNumber(String maskedCreditCardNumber) {
		this.maskedCreditCardNumber = maskedCreditCardNumber;
	}

	public Long getOrderPaymentId() {
		return this.orderPaymentId;
	}

	public void setOrderPaymentId(Long orderPaymentId) {
		this.orderPaymentId = orderPaymentId;
	}

	public String getPaymentCode() {
		return this.paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getPaymentConfirmationNumber() {
		return this.paymentConfirmationNumber;
	}

	public void setPaymentConfirmationNumber(String paymentConfirmationNumber) {
		this.paymentConfirmationNumber = paymentConfirmationNumber;
	}

	public Boolean getPaymentInstallmentFormFlag() {
		return this.paymentInstallmentFormFlag;
	}

	public void setPaymentInstallmentFormFlag(Boolean paymentInstallmentFormFlag) {
		this.paymentInstallmentFormFlag = paymentInstallmentFormFlag;
	}

	public Long getPaymentStatusId() {
		return this.paymentStatusId;
	}

	public void setPaymentStatusId(Long paymentStatusId) {
		this.paymentStatusId = paymentStatusId;
	}

	public Timestamp getPaymentTimestamp() {
		return this.paymentTimestamp;
	}

	public void setPaymentTimestamp(Timestamp paymentTimestamp) {
		this.paymentTimestamp = paymentTimestamp;
	}

	public Long getPaymentTypeId() {
		return this.paymentTypeId;
	}

	public void setPaymentTypeId(Long paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getThreeDsSecurityAuthLevel() {
		return this.threeDsSecurityAuthLevel;
	}

	public void setThreeDsSecurityAuthLevel(String threeDsSecurityAuthLevel) {
		this.threeDsSecurityAuthLevel = threeDsSecurityAuthLevel;
	}

	public String getVirtualAccountNumber() {
		return this.virtualAccountNumber;
	}

	public void setVirtualAccountNumber(String virtualAccountNumber) {
		this.virtualAccountNumber = virtualAccountNumber;
	}

	public String getWcsPaymentId() {
		return this.wcsPaymentId;
	}

	public void setWcsPaymentId(String wcsPaymentId) {
		this.wcsPaymentId = wcsPaymentId;
	}

	public VenMasterChangeType getVenMasterChangeType() {
		return this.venMasterChangeType;
	}

	public void setVenMasterChangeType(VenMasterChangeType venMasterChangeType) {
		this.venMasterChangeType = venMasterChangeType;
	}
	
}