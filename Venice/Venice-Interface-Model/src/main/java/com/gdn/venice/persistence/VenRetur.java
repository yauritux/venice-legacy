package com.gdn.venice.persistence;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * The persistent class for the ven_retur database table.
 * 
 */
@Entity
@Table(name="ven_retur")
public class VenRetur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_retur")  
	@TableGenerator(name="ven_retur", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="retur_id")
	private Long returId;

	private BigDecimal amount;

	@Column(name="blocked_flag")
	private Boolean blockedFlag;

	@Column(name="blocked_reason")
	private String blockedReason;

	@Column(name="blocked_timestamp")
	private Timestamp blockedTimestamp;

	@Column(name="blocking_source_id")
	private Long blockingSourceId;

	@Column(name="delivered_date_time")
	private Timestamp deliveredDateTime;

	@Column(name="finance_reconcile_flag")
	private Boolean financeReconcileFlag;

	@Column(name="fraud_check_status_id")
	private Long fraudCheckStatusId;

	@Column(name="fulfillment_status")
	private Long fulfillmentStatus;

	@Column(name="ip_address")
	private String ipAddress;

	@Column(name="retur_date")
	private Timestamp returDate;

	@Column(name="retur_timestamp")
	private Timestamp returTimestamp;

	@Column(name="rma_action")
	private String rmaAction;

	@Column(name="rma_flag")
	private Boolean rmaFlag;

	@Column(name="wcs_retur_id")
	private String wcsReturId;

	//bi-directional many-to-one association to VenReturItem
	@OneToMany(mappedBy="venRetur")
	private List<VenReturItem> venReturItems;
	
	//bi-directional many-to-one association to VenCustomer
    @ManyToOne
	@JoinColumn(name="customer_id")
	private VenCustomer venCustomer;
    
	//bi-directional many-to-one association to VenReturStatus
    @ManyToOne
	@JoinColumn(name="retur_status_id", nullable=false)
	private VenOrderStatus venReturStatus;
    
  	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venRetur")
	private List<VenReturAddress> venReturAddresses;
	
	//bi-directional many-to-one association to VenOrderItem
	@OneToMany(mappedBy="venRetur")
	private List<VenReturContactDetail> venReturContactDetails;

    public VenRetur() {
    }

	public Long getReturId() {
		return this.returId;
	}

	public void setReturId(Long returId) {
		this.returId = returId;
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

	public Long getBlockingSourceId() {
		return this.blockingSourceId;
	}

	public void setBlockingSourceId(Long blockingSourceId) {
		this.blockingSourceId = blockingSourceId;
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

	public Timestamp getReturDate() {
		return this.returDate;
	}

	public void setReturDate(Timestamp returDate) {
		this.returDate = returDate;
	}

	public Timestamp getReturTimestamp() {
		return this.returTimestamp;
	}

	public void setReturTimestamp(Timestamp returTimestamp) {
		this.returTimestamp = returTimestamp;
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

	public String getWcsReturId() {
		return this.wcsReturId;
	}

	public void setWcsReturId(String wcsReturId) {
		this.wcsReturId = wcsReturId;
	}

	public List<VenReturItem> getVenReturItems() {
		return this.venReturItems;
	}

	public void setVenReturItems(List<VenReturItem> venReturItems) {
		this.venReturItems = venReturItems;
	}

	public VenCustomer getVenCustomer() {
		return venCustomer;
	}

	public void setVenCustomer(VenCustomer venCustomer) {
		this.venCustomer = venCustomer;
	}

	public VenOrderStatus getVenReturStatus() {
		return venReturStatus;
	}

	public void setVenReturStatus(VenOrderStatus venReturStatus) {
		this.venReturStatus = venReturStatus;
	}
	
	public List<VenReturAddress> getVenReturAddresses() {
		return venReturAddresses;
	}

	public void setVenReturAddresses(List<VenReturAddress> venReturAddresses) {
		this.venReturAddresses = venReturAddresses;
	}

	public List<VenReturContactDetail> getVenReturContactDetails() {
		return venReturContactDetails;
	}

	public void setVenReturContactDetails(
			List<VenReturContactDetail> venReturContactDetails) {
		this.venReturContactDetails = venReturContactDetails;
	}
	
}