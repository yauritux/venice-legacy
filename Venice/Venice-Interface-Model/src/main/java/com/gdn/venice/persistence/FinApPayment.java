package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the fin_ap_payment database table.
 * 
 */
@Entity
@Table(name="fin_ap_payment")
public class FinApPayment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ap_payment")  
	@TableGenerator(name="fin_ap_payment", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="ap_payment_id", unique=true, nullable=false)
	private Long apPaymentId;

	@Column(nullable=false, precision=20, scale=2)
	private BigDecimal amount;

	@Column(name="pph23_amount", precision=20, scale=2)
	private BigDecimal pph23Amount;
	
	@Column(name="penalty_amount", precision=20, scale=2)
	private BigDecimal penaltyAmount;

	//bi-directional many-to-one association to FinApInvoice
	@OneToMany(mappedBy="finApPayment")
	private List<FinApInvoice> finApInvoices;

	//bi-directional many-to-one association to FinApManualJournalTransaction
	@OneToMany(mappedBy="finApPayment")
	private List<FinApManualJournalTransaction> finApManualJournalTransactions;

	//bi-directional many-to-one association to FinAccount
    @ManyToOne
	@JoinColumn(name="bank_account_id", nullable=false)
	private FinAccount finAccount;

	//bi-directional many-to-one association to FinApPaymentType
    @ManyToOne
	@JoinColumn(name="payment_type_id", nullable=false)
	private FinApPaymentType finApPaymentType;

	//bi-directional many-to-one association to FinApprovalStatus
    @ManyToOne
	@JoinColumn(name="approval_status_id", nullable=false)
	private FinApprovalStatus finApprovalStatus;

	//bi-directional many-to-one association to FinPeriod
    @ManyToOne
	@JoinColumn(name="period_id", nullable=false)
	private FinPeriod finPeriod;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="payee_party_id", nullable=false)
	private VenParty venParty;

	//bi-directional many-to-one association to FinArFundsInRefund
	@OneToMany(mappedBy="finApPayment")
	private List<FinArFundsInRefund> finArFundsInRefunds;

	//bi-directional many-to-many association to FinJournalTransaction
	@ManyToMany(mappedBy="finApPayments")//, fetch=FetchType.EAGER)
	private List<FinJournalTransaction> finJournalTransactions;

	//bi-directional many-to-one association to FinSalesRecord
	@OneToMany(mappedBy="finApPayment")
	private List<FinSalesRecord> finSalesRecords;
	
    public FinApPayment() {
    }

	public Long getApPaymentId() {
		return this.apPaymentId;
	}

	public void setApPaymentId(Long apPaymentId) {
		this.apPaymentId = apPaymentId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPph23Amount() {
		return pph23Amount;
	}

	public void setPph23Amount(BigDecimal pph23Amount) {
		this.pph23Amount = pph23Amount;
	}

	public BigDecimal getPenaltyAmount() {
		return this.penaltyAmount;
	}

	public void setPenaltyAmount(BigDecimal penaltyAmount) {
		this.penaltyAmount = penaltyAmount;
	}

	public List<FinApInvoice> getFinApInvoices() {
		return this.finApInvoices;
	}

	public void setFinApInvoices(List<FinApInvoice> finApInvoices) {
		this.finApInvoices = finApInvoices;
	}
	
	public List<FinApManualJournalTransaction> getFinApManualJournalTransactions() {
		return this.finApManualJournalTransactions;
	}

	public void setFinApManualJournalTransactions(List<FinApManualJournalTransaction> finApManualJournalTransactions) {
		this.finApManualJournalTransactions = finApManualJournalTransactions;
	}
	
	public FinAccount getFinAccount() {
		return this.finAccount;
	}

	public void setFinAccount(FinAccount finAccount) {
		this.finAccount = finAccount;
	}
	
	public FinApPaymentType getFinApPaymentType() {
		return this.finApPaymentType;
	}

	public void setFinApPaymentType(FinApPaymentType finApPaymentType) {
		this.finApPaymentType = finApPaymentType;
	}
	
	public FinApprovalStatus getFinApprovalStatus() {
		return this.finApprovalStatus;
	}

	public void setFinApprovalStatus(FinApprovalStatus finApprovalStatus) {
		this.finApprovalStatus = finApprovalStatus;
	}
	
	public FinPeriod getFinPeriod() {
		return this.finPeriod;
	}

	public void setFinPeriod(FinPeriod finPeriod) {
		this.finPeriod = finPeriod;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<FinArFundsInRefund> getFinArFundsInRefunds() {
		return this.finArFundsInRefunds;
	}

	public void setFinArFundsInRefunds(List<FinArFundsInRefund> finArFundsInRefunds) {
		this.finArFundsInRefunds = finArFundsInRefunds;
	}
	
	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
	public List<FinSalesRecord> getFinSalesRecords() {
		return this.finSalesRecords;
	}

	public void setFinSalesRecords(List<FinSalesRecord> finSalesRecords) {
		this.finSalesRecords = finSalesRecords;
	}
}