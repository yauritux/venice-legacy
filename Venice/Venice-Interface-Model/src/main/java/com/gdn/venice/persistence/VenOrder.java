package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the ven_order database table.
 * 
 */
@Entity
@Table(name="ven_order")
public class VenOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order")  
	@TableGenerator(name="ven_order", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_id", unique=true, nullable=false)
	private Long orderId;

	@Column(precision=20)
	private BigDecimal amount;

	@Column(name="blocked_flag")
	private Boolean blockedFlag;

	@Column(name="blocked_reason", length=1000)
	private String blockedReason;

	@Column(name="blocked_timestamp")
	private Timestamp blockedTimestamp;

	@Column(name="delivered_date_time")
	private Timestamp deliveredDateTime;

	@Column(name="finance_reconcile_flag")
	private Boolean financeReconcileFlag;

	@Column(name="fulfillment_status")
	private Long fulfillmentStatus;

	@Column(name="ip_address", length=100)
	private String ipAddress;

	@Column(name="order_timestamp", nullable=false)
	private Timestamp orderTimestamp;
	
	@Column(name="order_date")
	private Timestamp orderDate;

	@Column(name="rma_action", length=10)
	private String rmaAction;

	@Column(name="rma_flag")
	private Boolean rmaFlag;

	@Column(name="wcs_order_id", nullable=false, length=100)
	private String wcsOrderId;

	//bi-directional many-to-one association to FinApManualJournalTransaction
	@OneToMany(mappedBy="venOrder")
	private List<FinApManualJournalTransaction> finApManualJournalTransactions;

	//bi-directional many-to-one association to FrdFraudSuspicionCase
	@OneToMany(mappedBy="venOrder")
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases1;

	//bi-directional many-to-many association to FrdFraudSuspicionCase
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="frd_fraud_related_order_info"
		, joinColumns={
			@JoinColumn(name="order_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="fraud_suspicion_case_id", nullable=false)
			}
		)
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases2;

	//bi-directional many-to-one association to VenCustomer
    @ManyToOne
	@JoinColumn(name="customer_id")
	private VenCustomer venCustomer;

	//bi-directional many-to-one association to VenFraudCheckStatus
    @ManyToOne
	@JoinColumn(name="fraud_check_status_id")
	private VenFraudCheckStatus venFraudCheckStatus;

	//bi-directional many-to-one association to VenOrderBlockingSource
    @ManyToOne
	@JoinColumn(name="blocking_source_id")
	private VenOrderBlockingSource venOrderBlockingSource;

	//bi-directional many-to-one association to VenOrderStatus
    @ManyToOne
	@JoinColumn(name="order_status_id", nullable=false)
	private VenOrderStatus venOrderStatus;

	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venOrder")
	private List<VenOrderItem> venOrderItems;

	//bi-directional many-to-one association to VenOrderPaymentAllocation
	@OneToMany(mappedBy="venOrder")
	private List<VenOrderPaymentAllocation> venOrderPaymentAllocations;

	//bi-directional many-to-one association to VenOrderStatusHistory
	@OneToMany(mappedBy="venOrder")
	private List<VenOrderStatusHistory> venOrderStatusHistories;

	//bi-directional many-to-one association to VenTransactionFee
	@OneToMany(mappedBy="venOrder")
	private List<VenTransactionFee> venTransactionFees;

	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venOrder")
	private List<VenOrderAddress> venOrderAddresses;
	
	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venOrder")
	private List<VenOrderContactDetail> venOrderContactDetails;
	
    public VenOrder() {
    }

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getBlockedFlag() {
		return this.blockedFlag;
	}

	public void setBlockedFlag(Boolean blockedFlag) {
		this.blockedFlag = blockedFlag;
	}

	public String getBlockedReason() {
		return this.blockedReason;
	}

	public void setBlockedReason(String blockedReason) {
		this.blockedReason = blockedReason;
	}

	public Timestamp getBlockedTimestamp() {
		return this.blockedTimestamp;
	}

	public void setBlockedTimestamp(Timestamp blockedTimestamp) {
		this.blockedTimestamp = blockedTimestamp;
	}

	public Timestamp getDeliveredDateTime() {
		return this.deliveredDateTime;
	}

	public void setDeliveredDateTime(Timestamp deliveredDateTime) {
		this.deliveredDateTime = deliveredDateTime;
	}

	public Boolean getFinanceReconcileFlag() {
		return this.financeReconcileFlag;
	}

	public void setFinanceReconcileFlag(Boolean financeReconcileFlag) {
		this.financeReconcileFlag = financeReconcileFlag;
	}

	public Long getFulfillmentStatus() {
		return this.fulfillmentStatus;
	}

	public void setFulfillmentStatus(Long fulfillmentStatus) {
		this.fulfillmentStatus = fulfillmentStatus;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Timestamp getOrderTimestamp() {
		return this.orderTimestamp;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderTimestamp(Timestamp orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}

	public String getRmaAction() {
		return this.rmaAction;
	}

	public void setRmaAction(String rmaAction) {
		this.rmaAction = rmaAction;
	}

	public Boolean getRmaFlag() {
		return this.rmaFlag;
	}

	public void setRmaFlag(Boolean rmaFlag) {
		this.rmaFlag = rmaFlag;
	}

	public String getWcsOrderId() {
		return this.wcsOrderId;
	}

	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}

	public List<FinApManualJournalTransaction> getFinApManualJournalTransactions() {
		return this.finApManualJournalTransactions;
	}

	public void setFinApManualJournalTransactions(List<FinApManualJournalTransaction> finApManualJournalTransactions) {
		this.finApManualJournalTransactions = finApManualJournalTransactions;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases1() {
		return this.frdFraudSuspicionCases1;
	}

	public void setFrdFraudSuspicionCases1(List<FrdFraudSuspicionCase> frdFraudSuspicionCases1) {
		this.frdFraudSuspicionCases1 = frdFraudSuspicionCases1;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases2() {
		return this.frdFraudSuspicionCases2;
	}

	public void setFrdFraudSuspicionCases2(List<FrdFraudSuspicionCase> frdFraudSuspicionCases2) {
		this.frdFraudSuspicionCases2 = frdFraudSuspicionCases2;
	}
	
	public VenCustomer getVenCustomer() {
		return this.venCustomer;
	}

	public void setVenCustomer(VenCustomer venCustomer) {
		this.venCustomer = venCustomer;
	}
	
	public VenFraudCheckStatus getVenFraudCheckStatus() {
		return this.venFraudCheckStatus;
	}

	public void setVenFraudCheckStatus(VenFraudCheckStatus venFraudCheckStatus) {
		this.venFraudCheckStatus = venFraudCheckStatus;
	}
	
	public VenOrderBlockingSource getVenOrderBlockingSource() {
		return this.venOrderBlockingSource;
	}

	public void setVenOrderBlockingSource(VenOrderBlockingSource venOrderBlockingSource) {
		this.venOrderBlockingSource = venOrderBlockingSource;
	}
	
	public VenOrderStatus getVenOrderStatus() {
		return this.venOrderStatus;
	}

	public void setVenOrderStatus(VenOrderStatus venOrderStatus) {
		this.venOrderStatus = venOrderStatus;
	}
	
	public List<VenOrderItem> getVenOrderItems() {
		return this.venOrderItems;
	}

	public void setVenOrderItems(List<VenOrderItem> venOrderItems) {
		this.venOrderItems = venOrderItems;
	}
	
	public List<VenOrderPaymentAllocation> getVenOrderPaymentAllocations() {
		return this.venOrderPaymentAllocations;
	}

	public void setVenOrderPaymentAllocations(List<VenOrderPaymentAllocation> venOrderPaymentAllocations) {
		this.venOrderPaymentAllocations = venOrderPaymentAllocations;
	}
	
	public List<VenOrderStatusHistory> getVenOrderStatusHistories() {
		return this.venOrderStatusHistories;
	}

	public void setVenOrderStatusHistories(List<VenOrderStatusHistory> venOrderStatusHistories) {
		this.venOrderStatusHistories = venOrderStatusHistories;
	}
	
	public List<VenTransactionFee> getVenTransactionFees() {
		return this.venTransactionFees;
	}

	public void setVenTransactionFees(List<VenTransactionFee> venTransactionFees) {
		this.venTransactionFees = venTransactionFees;
	}

	public List<VenOrderAddress> getVenOrderAddresses() {
		return venOrderAddresses;
	}

	public void setVenOrderAddresses(List<VenOrderAddress> venOrderAddresses) {
		this.venOrderAddresses = venOrderAddresses;
	}

	public List<VenOrderContactDetail> getVenOrderContactDetails() {
		return venOrderContactDetails;
	}

	public void setVenOrderContactDetails(
			List<VenOrderContactDetail> venOrderContactDetails) {
		this.venOrderContactDetails = venOrderContactDetails;
	}
	
}