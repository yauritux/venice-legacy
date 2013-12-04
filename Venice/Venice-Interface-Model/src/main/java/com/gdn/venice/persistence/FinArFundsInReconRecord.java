package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the fin_ar_funds_in_recon_record database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_recon_record")
public class FinArFundsInReconRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_in_recon_record")  
	@TableGenerator(name="fin_ar_funds_in_recon_record", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="reconciliation_record_id", unique=true, nullable=false)
	private Long reconciliationRecordId;

	@Column(length=1000)
	private String comment;

    @Temporal( TemporalType.DATE)
	@Column(name="order_date")
	private Date orderDate;

	@Column(name="payment_confirmation_number", length=100)
	private String paymentConfirmationNumber;

	@Column(name="provider_report_fee_amount", precision=20, scale=2)
	private BigDecimal providerReportFeeAmount;

	@Column(name="provider_report_paid_amount", precision=20, scale=2)
	private BigDecimal providerReportPaidAmount;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="provider_report_payment_date",nullable=true)
	private Date providerReportPaymentDate;

	@Column(name="provider_report_payment_id", length=100,nullable=true)
	private String providerReportPaymentId;

	//updated by Olive Hibernate needs temporal data type to be Date or Calendar
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="reconcilliation_record_timestamp", nullable=true)
	private Date reconcilliationRecordTimestamp;

	@Column(name="refund_amount", precision=20, scale=2)
	private BigDecimal refundAmount;
	
	@Column(name="remaining_balance_amount", precision=20, scale=2)
	private BigDecimal remainingBalanceAmount;

	@Column(name="user_logon_name", length=100)
	private String userLogonName;

	@Column(name="wcs_order_id", length=100)
	private String wcsOrderId;

	@Column(name="payment_amount", precision=20, scale=2)
	private BigDecimal paymentAmount;
	
	@Column(name="nomor_reff", length=100)
	private String nomorReff;

	//updated Arifin
	@Column(name="unique_payment", length=100)
	private String uniquePayment;	

	//bi-directional many-to-one association to FinArFundsInReconComment
	@OneToMany(mappedBy="finArFundsInReconRecord")
	private List<FinArFundsInReconComment> finArFundsInReconComments;

	//bi-directional many-to-one association to FinApprovalStatus
    @ManyToOne
	@JoinColumn(name="approval_status_id", nullable=false)
	private FinApprovalStatus finApprovalStatus;

	//bi-directional many-to-one association to FinArFundsInActionApplied
    @ManyToOne
	@JoinColumn(name="action_applied_id", nullable=false)
	private FinArFundsInActionApplied finArFundsInActionApplied;

	//bi-directional many-to-one association to FinArFundsInReport
    @ManyToOne
	@JoinColumn(name="payment_report_id",nullable=true)
	private FinArFundsInReport finArFundsInReport;

	//bi-directional many-to-one association to FinArReconResult
    @ManyToOne
	@JoinColumn(name="recon_result_id", nullable=false)
	private FinArReconResult finArReconResult;

	//bi-directional many-to-one association to VenOrderPayment
    @ManyToOne
	@JoinColumn(name="order_payment_id")
	private VenOrderPayment venOrderPayment;

	//bi-directional many-to-one association to FinArFundsInRefund
	@OneToMany(mappedBy="finArFundsInReconRecord")
	private List<FinArFundsInRefund> finArFundsInRefunds;

	//bi-directional many-to-many association to FinJournalTransaction
	@ManyToMany(mappedBy="finArFundsInReconRecords")//, fetch=FetchType.EAGER)
	private List<FinJournalTransaction> finJournalTransactions;
	
	//bi-directional many-to-one association to FinArFundsInJournalTransaction
	@OneToMany(mappedBy="finArFundsInReconRecords")
	private List<FinArFundsInJournalTransaction> finArFundsInJournalTransactions;
	
	//bi-directional many-to-one association to FinArFundsInActionAppliedHistory
	@OneToMany(mappedBy="finArFundsInReconRecords")
	private List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistory;
	
	//bi-directional many-to-one association to FinArFundsInActionApplied
    @ManyToOne
	@JoinColumn(name="report_time_id", nullable=true)
	private FinArFundsIdReportTime finArFundsIdReportTime;

	public FinArFundsInReconRecord() {
    }

	public Long getReconciliationRecordId() {
		return this.reconciliationRecordId;
	}

	public void setReconciliationRecordId(Long reconciliationRecordId) {
		this.reconciliationRecordId = reconciliationRecordId;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getPaymentConfirmationNumber() {
		return this.paymentConfirmationNumber;
	}

	public void setPaymentConfirmationNumber(String paymentConfirmationNumber) {
		this.paymentConfirmationNumber = paymentConfirmationNumber;
	}

	public BigDecimal getProviderReportFeeAmount() {
		return this.providerReportFeeAmount;
	}

	public void setProviderReportFeeAmount(BigDecimal providerReportFeeAmount) {
		this.providerReportFeeAmount = providerReportFeeAmount;
	}

	public BigDecimal getProviderReportPaidAmount() {
		return this.providerReportPaidAmount;
	}

	public void setProviderReportPaidAmount(BigDecimal providerReportPaidAmount) {
		this.providerReportPaidAmount = providerReportPaidAmount;
	}

	public Date getProviderReportPaymentDate() {
		return this.providerReportPaymentDate;
	}

	public void setProviderReportPaymentDate(Date providerReportPaymentDate) {
		this.providerReportPaymentDate = providerReportPaymentDate;
	}

	public String getProviderReportPaymentId() {
		return this.providerReportPaymentId;
	}

	public void setProviderReportPaymentId(String providerReportPaymentId) {
		this.providerReportPaymentId = providerReportPaymentId;
	}

	public Date getReconcilliationRecordTimestamp() {
		return this.reconcilliationRecordTimestamp;
	}

	public void setReconcilliationRecordTimestamp(Timestamp reconcilliationRecordTimestamp) {
		this.reconcilliationRecordTimestamp = reconcilliationRecordTimestamp;
	}

	public BigDecimal getRemainingBalanceAmount() {
		return this.remainingBalanceAmount;
	}

	public void setRemainingBalanceAmount(BigDecimal remainingBalanceAmount) {
		this.remainingBalanceAmount = remainingBalanceAmount;
	}

	public String getUserLogonName() {
		return this.userLogonName;
	}

	public void setUserLogonName(String userLogonName) {
		this.userLogonName = userLogonName;
	}

	public String getWcsOrderId() {
		return this.wcsOrderId;
	}

	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}

	public String getUniquePayment() {
		return uniquePayment;
	}

	public void setUniquePayment(String uniquePayment) {
		this.uniquePayment = uniquePayment;
	}
	
	public List<FinArFundsInReconComment> getFinArFundsInReconComments() {
		return this.finArFundsInReconComments;
	}

	public void setFinArFundsInReconComments(List<FinArFundsInReconComment> finArFundsInReconComments) {
		this.finArFundsInReconComments = finArFundsInReconComments;
	}
	
	public FinApprovalStatus getFinApprovalStatus() {
		return this.finApprovalStatus;
	}

	public void setFinApprovalStatus(FinApprovalStatus finApprovalStatus) {
		this.finApprovalStatus = finApprovalStatus;
	}
	
	public FinArFundsInActionApplied getFinArFundsInActionApplied() {
		return this.finArFundsInActionApplied;
	}

	public void setFinArFundsInActionApplied(FinArFundsInActionApplied finArFundsInActionApplied) {
		this.finArFundsInActionApplied = finArFundsInActionApplied;
	}
	
	public FinArFundsInReport getFinArFundsInReport() {
		return this.finArFundsInReport;
	}

	public void setFinArFundsInReport(FinArFundsInReport finArFundsInReport) {
		this.finArFundsInReport = finArFundsInReport;
	}
	
	public FinArReconResult getFinArReconResult() {
		return this.finArReconResult;
	}

	public void setFinArReconResult(FinArReconResult finArReconResult) {
		this.finArReconResult = finArReconResult;
	}
	
	public VenOrderPayment getVenOrderPayment() {
		return this.venOrderPayment;
	}

	public void setVenOrderPayment(VenOrderPayment venOrderPayment) {
		this.venOrderPayment = venOrderPayment;
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
		
	public BigDecimal getPaymentAmount() {
		return this.paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	public String getNomorReff() {
		return this.nomorReff;
	}

	public void setNomorReff(String nomorReff) {
		this.nomorReff = nomorReff;
	}	
	
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}	

    public List<FinArFundsInJournalTransaction> getFinArFundsInJournalTransactions() {
		return finArFundsInJournalTransactions;
	}

	public void setFinArFundsInJournalTransactions(
			List<FinArFundsInJournalTransaction> finArFundsInJournalTransactions) {
		this.finArFundsInJournalTransactions = finArFundsInJournalTransactions;
	}
	
	public FinArFundsIdReportTime getFinArFundsIdReportTime() {
		return finArFundsIdReportTime;
	}

	public void setFinArFundsIdReportTime(
			FinArFundsIdReportTime finArFundsIdReportTime) {
		this.finArFundsIdReportTime = finArFundsIdReportTime;
	}
	
	public List<FinArFundsInActionAppliedHistory> getFinArFundsInActionAppliedHistory() {
		return finArFundsInActionAppliedHistory;
	}

	public void setFinArFundsInActionAppliedHistory(
			List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistory) {
		this.finArFundsInActionAppliedHistory = finArFundsInActionAppliedHistory;
	}

}