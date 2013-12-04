package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ven_order_payment database table.
 * 
 */
@Entity
@Table(name="ven_order_payment")
public class VenOrderPayment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_payment")  
	@TableGenerator(name="ven_order_payment", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_payment_id", unique=true, nullable=false)
	private Long orderPaymentId;

	@Column(nullable=false, precision=20, scale=2)
	private BigDecimal amount;

	@Column(name="card_holder_name", length=100)
	private String cardHolderName;

	@Column(name="handling_fee", nullable=false, precision=20, scale=2)
	private BigDecimal handlingFee;

	@Column(precision=20, scale=2)
	private BigDecimal installment;

	@Column(precision=20, scale=2)
	private BigDecimal interest;

	@Column(name="interest_installment", precision=20, scale=2)
	private BigDecimal interestInstallment;

	@Column(name="internet_banking_id", length=100)
	private String internetBankingId;
	
	@Column(name="reference_id", length=100)
	private String referenceId;

	@Column(name="masked_credit_card_number", length=100)
	private String maskedCreditCardNumber;

	@Column(name="payment_confirmation_number", length=100)
	private String paymentConfirmationNumber;

	@Column(name="payment_installment_form_flag", nullable=false)
	private Boolean paymentInstallmentFormFlag;

	@Column(name="payment_timestamp", nullable=false)
	private Timestamp paymentTimestamp;

	private Integer tenor;

	@Column(name="three_ds_security_level_auth", length=2)
	private String threeDsSecurityLevelAuth;

	@Column(name="virtual_account_number", length=100)
	private String virtualAccountNumber;

	@Column(name="wcs_payment_id", nullable=false, length=100)
	private String wcsPaymentId;

	@Column(name="wcs_payment_type", length=100)
	private String wcsPaymentType;

	//bi-directional many-to-one association to FinArFundsInReconRecord
	@OneToMany(mappedBy="venOrderPayment")
	private List<FinArFundsInReconRecord> finArFundsInReconRecords;

	//bi-directional many-to-one association to VenMigsTransaction
	@OneToMany(mappedBy="venOrderPayment")
	private List<VenMigsTransaction> venMigsTransactions;

	//bi-directional many-to-many association to FrdFraudSuspicionCase
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="frd_payment_fraud_suspicion"
		, joinColumns={
			@JoinColumn(name="order_payment_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="fraud_suspicion_case_id", nullable=false)
			}
		)
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases;

	//bi-directional many-to-one association to VenAddress
    @ManyToOne
	@JoinColumn(name="billing_address_id")
	private VenAddress venAddress;

	//bi-directional many-to-one association to VenBank
    @ManyToOne
	@JoinColumn(name="bank_id", nullable=false)
	private VenBank venBank;

	//bi-directional many-to-one association to VenPaymentStatus
    @ManyToOne
	@JoinColumn(name="payment_status_id", nullable=false)
	private VenPaymentStatus venPaymentStatus;

	//bi-directional many-to-one association to VenPaymentType
    @ManyToOne
	@JoinColumn(name="payment_type_id", nullable=false)
	private VenPaymentType venPaymentType;

	//bi-directional many-to-one association to VenWcsPaymentType
    @ManyToOne
	@JoinColumn(name="wcs_payment_type_id", nullable=false)
	private VenWcsPaymentType venWcsPaymentType;

	//bi-directional many-to-one association to VenOrderPaymentAllocation
	@OneToMany(mappedBy="venOrderPayment")
	private List<VenOrderPaymentAllocation> venOrderPaymentAllocations;

	//bi-directional many-to-one association to VenOrderPaymentAttribute
	@OneToMany(mappedBy="venOrderPayment")
	private List<VenOrderPaymentAttribute> venOrderPaymentAttributes;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="old_order_id", nullable=true, unique=true)
	private VenOrder oldVenOrder;
	
    public VenOrderPayment() {
    }

    
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Long getOrderPaymentId() {
		return this.orderPaymentId;
	}

	public void setOrderPaymentId(Long orderPaymentId) {
		this.orderPaymentId = orderPaymentId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public BigDecimal getInstallment() {
		return this.installment;
	}

	public void setInstallment(BigDecimal installment) {
		this.installment = installment;
	}

	public BigDecimal getInterest() {
		return this.interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getInterestInstallment() {
		return this.interestInstallment;
	}

	public void setInterestInstallment(BigDecimal interestInstallment) {
		this.interestInstallment = interestInstallment;
	}

	public String getInternetBankingId() {
		return this.internetBankingId;
	}

	public void setInternetBankingId(String internetBankingId) {
		this.internetBankingId = internetBankingId;
	}

	public String getMaskedCreditCardNumber() {
		return this.maskedCreditCardNumber;
	}

	public void setMaskedCreditCardNumber(String maskedCreditCardNumber) {
		this.maskedCreditCardNumber = maskedCreditCardNumber;
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

	public Timestamp getPaymentTimestamp() {
		return this.paymentTimestamp;
	}

	public void setPaymentTimestamp(Timestamp paymentTimestamp) {
		this.paymentTimestamp = paymentTimestamp;
	}

	public Integer getTenor() {
		return this.tenor;
	}

	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}

	public String getThreeDsSecurityLevelAuth() {
		return this.threeDsSecurityLevelAuth;
	}

	public void setThreeDsSecurityLevelAuth(String threeDsSecurityLevelAuth) {
		this.threeDsSecurityLevelAuth = threeDsSecurityLevelAuth;
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

	public String getWcsPaymentType() {
		return this.wcsPaymentType;
	}

	public void setWcsPaymentType(String wcsPaymentType) {
		this.wcsPaymentType = wcsPaymentType;
	}

	public List<FinArFundsInReconRecord> getFinArFundsInReconRecords() {
		return this.finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(List<FinArFundsInReconRecord> finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}
	
	public List<VenMigsTransaction> getVenMigsTransactions() {
		return this.venMigsTransactions;
	}

	public void setVenMigsTransactions(List<VenMigsTransaction> venMigsTransactions) {
		this.venMigsTransactions = venMigsTransactions;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases() {
		return this.frdFraudSuspicionCases;
	}

	public void setFrdFraudSuspicionCases(List<FrdFraudSuspicionCase> frdFraudSuspicionCases) {
		this.frdFraudSuspicionCases = frdFraudSuspicionCases;
	}
	
	public VenAddress getVenAddress() {
		return this.venAddress;
	}

	public void setVenAddress(VenAddress venAddress) {
		this.venAddress = venAddress;
	}
	
	public VenBank getVenBank() {
		return this.venBank;
	}

	public void setVenBank(VenBank venBank) {
		this.venBank = venBank;
	}
	
	public VenPaymentStatus getVenPaymentStatus() {
		return this.venPaymentStatus;
	}

	public void setVenPaymentStatus(VenPaymentStatus venPaymentStatus) {
		this.venPaymentStatus = venPaymentStatus;
	}
	
	public VenPaymentType getVenPaymentType() {
		return this.venPaymentType;
	}

	public void setVenPaymentType(VenPaymentType venPaymentType) {
		this.venPaymentType = venPaymentType;
	}
	
	public VenWcsPaymentType getVenWcsPaymentType() {
		return this.venWcsPaymentType;
	}

	public void setVenWcsPaymentType(VenWcsPaymentType venWcsPaymentType) {
		this.venWcsPaymentType = venWcsPaymentType;
	}
	
	public List<VenOrderPaymentAllocation> getVenOrderPaymentAllocations() {
		return this.venOrderPaymentAllocations;
	}

	public void setVenOrderPaymentAllocations(List<VenOrderPaymentAllocation> venOrderPaymentAllocations) {
		this.venOrderPaymentAllocations = venOrderPaymentAllocations;
	}
	
	public List<VenOrderPaymentAttribute> getVenOrderPaymentAttributes() {
		return this.venOrderPaymentAttributes;
	}

	public void setVenOrderPaymentAttributes(List<VenOrderPaymentAttribute> venOrderPaymentAttributes) {
		this.venOrderPaymentAttributes = venOrderPaymentAttributes;
	}

	public VenOrder getOldVenOrder() {
		return oldVenOrder;
	}


	public void setOldVenOrder(VenOrder oldVenOrder) {
		this.oldVenOrder = oldVenOrder;
	}

	
}