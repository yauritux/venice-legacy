package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the fin_journal_transaction database table.
 * 
 */
@Entity
@Table(name="fin_journal_transaction")
public class FinJournalTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_journal_transaction")  
	@TableGenerator(name="fin_journal_transaction", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="transaction_id", unique=true, nullable=false)
	private Long transactionId;

	@Column(length=1000)
	private String comments;

	@Column(name="credit_debit_flag", nullable=false)
	private Boolean creditDebitFlag;

	@Column(name="transaction_amount", nullable=false, precision=20, scale=2)
	private BigDecimal transactionAmount;

	@Column(name="transaction_timestamp", nullable=false)
	private Timestamp transactionTimestamp;

	//bi-directional many-to-one association to FinApManualJournalTransaction
	@OneToMany(mappedBy="finJournalTransaction")
	private List<FinApManualJournalTransaction> finApManualJournalTransactions;

	//bi-directional many-to-one association to FinAccount
    @ManyToOne
	@JoinColumn(name="account_id", nullable=false)
	private FinAccount finAccount;

	//bi-directional many-to-many association to FinApInvoice
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="fin_ap_invoice_journal_transaction"
		, joinColumns={
			@JoinColumn(name="transaction_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="ap_invoice_id", nullable=false)
			}
		)
	private List<FinApInvoice> finApInvoices;

	//bi-directional many-to-many association to FinApPayment
    //update by olive to comment out fetchtype.eager 
    //read http://blog.eyallupu.com/2010/06/hibernate-exception-simultaneously.html for more info
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="fin_ap_payment_journal_transaction"
		, joinColumns={
			@JoinColumn(name="transaction_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="ap_payment_id", nullable=false)
			}
		)
	private List<FinApPayment> finApPayments;

	//bi-directional many-to-many association to FinArFundsInReconRecord
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="fin_ar_funds_in_journal_transaction"
		, joinColumns={
			@JoinColumn(name="transaction_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="reconciliation_record_id", nullable=false)
			}
		)
	private List<FinArFundsInReconRecord> finArFundsInReconRecords;

	//bi-directional many-to-one association to FinJournal
    @ManyToOne
	@JoinColumn(name="journal_id", nullable=false)
	private FinJournal finJournal;

	//bi-directional many-to-one association to FinJournalApprovalGroup
    @ManyToOne
	@JoinColumn(name="journal_group_id", nullable=false)
	private FinJournalApprovalGroup finJournalApprovalGroup;

	//bi-directional many-to-one association to FinPeriod
    @ManyToOne
	@JoinColumn(name="period_id", nullable=false)
	private FinPeriod finPeriod;

	//bi-directional many-to-one association to FinTransactionStatus
    @ManyToOne
	@JoinColumn(name="transaction_status_id", nullable=false)
	private FinTransactionStatus finTransactionStatus;

	//bi-directional many-to-one association to FinTransactionType
    @ManyToOne
	@JoinColumn(name="transaction_type_id", nullable=false)
	private FinTransactionType finTransactionType;

	//bi-directional many-to-many association to FinSalesRecord
	@ManyToMany(mappedBy="finJournalTransactions")//, fetch=FetchType.EAGER)
	private List<FinSalesRecord> finSalesRecords;
	
	//bi-directional many-to-one association to FinArFundsInJournalTransaction
	@OneToMany(mappedBy="finJournalTransactions")
	private List<FinArFundsInJournalTransaction> finArFundsInJournalTransactions;
	
	//bi-directional many-to-one association to FinPeriod
    @ManyToOne
	@JoinColumn(name="bank_id")//, nullable=false)
	private VenBank venBank;
    
    @Column(name="group_journal",  nullable=true)
	private Long groupJournal;
    
	@Column(name="unpaid_transaction_amount", precision=20, scale=2)
	private BigDecimal unpaidTransactionAmount;

	
	public Long getGroupJournal() {
		return groupJournal;
	}

	public void setGroupJournal(Long groupJournal) {
		this.groupJournal = groupJournal;
	}

	public VenBank getVenBank() {
		return venBank;
	}

	public void setVenBank(VenBank venBank) {
		this.venBank = venBank;
	}

	@Column(name="wcs_order_id", length=100)
	private String wcsOrderID;

    public FinJournalTransaction() {
    }

	public Long getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getCreditDebitFlag() {
		return this.creditDebitFlag;
	}

	public void setCreditDebitFlag(Boolean creditDebitFlag) {
		this.creditDebitFlag = creditDebitFlag;
	}

	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Timestamp getTransactionTimestamp() {
		return this.transactionTimestamp;
	}

	public void setTransactionTimestamp(Timestamp transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
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
	
	public List<FinApInvoice> getFinApInvoices() {
		return this.finApInvoices;
	}

	public void setFinApInvoices(List<FinApInvoice> finApInvoices) {
		this.finApInvoices = finApInvoices;
	}
	
	public List<FinApPayment> getFinApPayments() {
		return this.finApPayments;
	}

	public void setFinApPayments(List<FinApPayment> finApPayments) {
		this.finApPayments = finApPayments;
	}
	
	public List<FinArFundsInReconRecord> getFinArFundsInReconRecords() {
		return this.finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(List<FinArFundsInReconRecord> finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}
	
	public FinJournal getFinJournal() {
		return this.finJournal;
	}

	public void setFinJournal(FinJournal finJournal) {
		this.finJournal = finJournal;
	}
	
	public FinJournalApprovalGroup getFinJournalApprovalGroup() {
		return this.finJournalApprovalGroup;
	}

	public void setFinJournalApprovalGroup(FinJournalApprovalGroup finJournalApprovalGroup) {
		this.finJournalApprovalGroup = finJournalApprovalGroup;
	}
	
	public FinPeriod getFinPeriod() {
		return this.finPeriod;
	}

	public void setFinPeriod(FinPeriod finPeriod) {
		this.finPeriod = finPeriod;
	}
	
	public FinTransactionStatus getFinTransactionStatus() {
		return this.finTransactionStatus;
	}

	public void setFinTransactionStatus(FinTransactionStatus finTransactionStatus) {
		this.finTransactionStatus = finTransactionStatus;
	}
	
	public FinTransactionType getFinTransactionType() {
		return this.finTransactionType;
	}

	public void setFinTransactionType(FinTransactionType finTransactionType) {
		this.finTransactionType = finTransactionType;
	}
	
	public List<FinSalesRecord> getFinSalesRecords() {
		return this.finSalesRecords;
	}

	public void setFinSalesRecords(List<FinSalesRecord> finSalesRecords) {
		this.finSalesRecords = finSalesRecords;
	}
	
	public String getWcsOrderID() {
		return this.wcsOrderID;
	}

	public void setWcsOrderID(String wcsOrderID) {
		this.wcsOrderID = wcsOrderID;
	}	
	
	public List<FinArFundsInJournalTransaction> getFinArFundsInJournalTransactions() {
		return finArFundsInJournalTransactions;
	}

	public void setFinArFundsInJournalTransactions(
			List<FinArFundsInJournalTransaction> finArFundsInJournalTransactions) {
		this.finArFundsInJournalTransactions = finArFundsInJournalTransactions;
	}

	public BigDecimal getUnpaidTransactionAmount() {
		return unpaidTransactionAmount;
	}

	public void setUnpaidTransactionAmount(BigDecimal unpaidTransactionAmount) {
		this.unpaidTransactionAmount = unpaidTransactionAmount;
	}

	
}