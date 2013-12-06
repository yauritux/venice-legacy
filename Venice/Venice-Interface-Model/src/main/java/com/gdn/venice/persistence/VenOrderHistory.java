package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;


/**
 * The persistent class for the ven_order_history database table.
 * 
 */
@Entity
@Table(name="ven_order_history")
public class VenOrderHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_order_history")  
	@TableGenerator(name="ven_order_history", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="order_history_id", unique=true, nullable=false)
	private Long orderHistoryId;

	@Column(nullable=false, precision=20, scale=2)
	private BigDecimal amount;

	@Column(name="billing_address_id", nullable=false)
	private Long billingAddressId;

	@Column(name="billing_amount", nullable=false, precision=20, scale=2)
	private BigDecimal billingAmount;

	@Column(name="billing_method_id", nullable=false)
	private Long billingMethodId;

	@Column(name="blocked_flag", nullable=false)
	private Boolean blockedFlag;

	@Column(name="blocked_timestamp", nullable=false)
	private Timestamp blockedTimestamp;

	@Column(name="blocking_source_id", nullable=false)
	private Long blockingSourceId;

	@Column(name="customer_id", nullable=false)
	private Long customerId;

	@Column(name="delivered_date_time", nullable=false)
	private Timestamp deliveredDateTime;

	@Column(name="finance_reconcile_flag", nullable=false)
	private Boolean financeReconcileFlag;

	@Column(name="fraud_check_status_id", nullable=false)
	private Long fraudCheckStatusId;

	@Column(name="history_timestamp", nullable=false)
	private Timestamp historyTimestamp;

	@Column(name="ip_address", nullable=false, length=100)
	private String ipAddress;

	@Column(name="order_date_time", nullable=false)
	private Timestamp orderDateTime;

	@Column(name="order_id", nullable=false)
	private Long orderId;

	@Column(name="order_status_id", nullable=false)
	private Long orderStatusId;

	@Column(name="partial_fulfillment_flag", nullable=false)
	private Boolean partialFulfillmentFlag;

	@Column(name="wcs_billing_id", nullable=false, length=100)
	private String wcsBillingId;

	@Column(name="wcs_order_id", nullable=false, length=100)
	private String wcsOrderId;

	@Column(name="wcs_rma_id", length=100)
	private String wcsRmaId;

	@Column(name="wcs_rma_timestamp")
	private Timestamp wcsRmaTimestamp;

	//bi-directional many-to-one association to VenMasterChangeType
    @ManyToOne
	@JoinColumn(name="master_change_id", nullable=false)
	private VenMasterChangeType venMasterChangeType;

    public VenOrderHistory() {
    }

	public Long getOrderHistoryId() {
		return this.orderHistoryId;
	}

	public void setOrderHistoryId(Long orderHistoryId) {
		this.orderHistoryId = orderHistoryId;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getBillingAddressId() {
		return this.billingAddressId;
	}

	public void setBillingAddressId(Long billingAddressId) {
		this.billingAddressId = billingAddressId;
	}

	public BigDecimal getBillingAmount() {
		return this.billingAmount;
	}

	public void setBillingAmount(BigDecimal billingAmount) {
		this.billingAmount = billingAmount;
	}

	public Long getBillingMethodId() {
		return this.billingMethodId;
	}

	public void setBillingMethodId(Long billingMethodId) {
		this.billingMethodId = billingMethodId;
	}

	public Boolean getBlockedFlag() {
		return this.blockedFlag;
	}

	public void setBlockedFlag(Boolean blockedFlag) {
		this.blockedFlag = blockedFlag;
	}

	public Timestamp getBlockedTimestamp() {
		return this.blockedTimestamp;
	}

	public void setBlockedTimestamp(Timestamp blockedTimestamp) {
		this.blockedTimestamp = blockedTimestamp;
	}

	public Long getBlockingSourceId() {
		return this.blockingSourceId;
	}

	public void setBlockingSourceId(Long blockingSourceId) {
		this.blockingSourceId = blockingSourceId;
	}

	public Long getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public Long getFraudCheckStatusId() {
		return this.fraudCheckStatusId;
	}

	public void setFraudCheckStatusId(Long fraudCheckStatusId) {
		this.fraudCheckStatusId = fraudCheckStatusId;
	}

	public Timestamp getHistoryTimestamp() {
		return this.historyTimestamp;
	}

	public void setHistoryTimestamp(Timestamp historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Timestamp getOrderDateTime() {
		return this.orderDateTime;
	}

	public void setOrderDateTime(Timestamp orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public Long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderStatusId() {
		return this.orderStatusId;
	}

	public void setOrderStatusId(Long orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Boolean getPartialFulfillmentFlag() {
		return this.partialFulfillmentFlag;
	}

	public void setPartialFulfillmentFlag(Boolean partialFulfillmentFlag) {
		this.partialFulfillmentFlag = partialFulfillmentFlag;
	}

	public String getWcsBillingId() {
		return this.wcsBillingId;
	}

	public void setWcsBillingId(String wcsBillingId) {
		this.wcsBillingId = wcsBillingId;
	}

	public String getWcsOrderId() {
		return this.wcsOrderId;
	}

	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}

	public String getWcsRmaId() {
		return this.wcsRmaId;
	}

	public void setWcsRmaId(String wcsRmaId) {
		this.wcsRmaId = wcsRmaId;
	}

	public Timestamp getWcsRmaTimestamp() {
		return this.wcsRmaTimestamp;
	}

	public void setWcsRmaTimestamp(Timestamp wcsRmaTimestamp) {
		this.wcsRmaTimestamp = wcsRmaTimestamp;
	}

	public VenMasterChangeType getVenMasterChangeType() {
		return this.venMasterChangeType;
	}

	public void setVenMasterChangeType(VenMasterChangeType venMasterChangeType) {
		this.venMasterChangeType = venMasterChangeType;
	}
	
}