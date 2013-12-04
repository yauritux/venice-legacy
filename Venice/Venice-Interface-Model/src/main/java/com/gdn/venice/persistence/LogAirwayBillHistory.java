package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the log_airway_bill_history database table.
 * 
 */
@Entity
@Table(name="log_airway_bill_history")
public class LogAirwayBillHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_airway_bill_history")  
	@TableGenerator(name="log_airway_bill_history", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="airway_bill_history_id", unique=true, nullable=false)
	private Long airwayBillHistoryId;

	@Column(name="activity_approval_status_id", nullable=false)
	private Long activityApprovalStatusId;

	@Column(length=300)
	private String address;

	@Column(name="airway_bill_pickup_date_time")
	private Timestamp airwayBillPickupDateTime;

	@Column(name="airway_bill_timestamp", nullable=false)
	private Timestamp airwayBillTimestamp;

	@Column(name="awb_number", length=200)
	private String awbNumber;

	@Column(name="awb_status_id", nullable=false)
	private Long awbStatusId;

	@Column(length=100)
	private String consignee;

	@Column(name="contact_person", length=100)
	private String contactPerson;

	@Column(length=1000)
	private String content;

    @Temporal( TemporalType.DATE)
	@Column(name="date_of_return")
	private Date dateOfReturn;

	@Column(name="delivery_order", length=100)
	private String deliveryOrder;

	@Column(name="dest_code", length=100)
	private String destCode;

	@Column(length=100)
	private String destination;

	@Column(name="distribution_cart_id", nullable=false)
	private Long distributionCartId;

	@Column(name="gdn_reference", length=100)
	private String gdnReference;

	@Column(name="gift_wrap_charge", precision=20, scale=2)
	private BigDecimal giftWrapCharge;

	@Column(name="history_timestamp", nullable=false)
	private Timestamp historyTimestamp;

	@Column(name="insurance_charge", precision=20, scale=2)
	private BigDecimal insuranceCharge;

	@Column(name="invoice_approval_status_id", nullable=false)
	private Long invoiceApprovalStatusId;

	@Column(name="logistics_provider_id", nullable=false)
	private Long logisticsProviderId;

	@Column(name="note_return", length=1000)
	private String noteReturn;

	@Column(name="note_undelivered", nullable=false, length=1000)
	private String noteUndelivered;

	@Column(name="order_item_id", nullable=false)
	private Long orderItemId;

	@Column(name="other_charge", precision=20, scale=2)
	private BigDecimal otherCharge;

	@Column(name="package_weight", precision=20, scale=2)
	private BigDecimal packageWeight;

	@Column(length=100)
	private String pieces;

	@Column(name="price_per_kg", precision=20, scale=2)
	private BigDecimal pricePerKg;

    @Temporal( TemporalType.DATE)
	private Date received;

	@Column(length=100)
	private String recipient;

	@Column(length=100)
	private String relation;

	@Column(name="return", length=100)
	private String return_;

	@Column(length=100)
	private String service;

	@Column(length=200)
	private String shipper;

	@Column(length=100)
	private String status;

	@Column(length=100)
	private String tariff;

	@Column(name="total_charge", precision=20, scale=2)
	private BigDecimal totalCharge;

	@Column(name="tracking_number", length=100)
	private String trackingNumber;

	@Column(length=100)
	private String type;

    @Temporal( TemporalType.DATE)
	private Date undelivered;

	@Column(length=100)
	private String zip;

	//bi-directional many-to-one association to VenMasterChangeType
    @ManyToOne
	@JoinColumn(name="master_change_id", nullable=false)
	private VenMasterChangeType venMasterChangeType;

    public LogAirwayBillHistory() {
    }

	public Long getAirwayBillHistoryId() {
		return this.airwayBillHistoryId;
	}

	public void setAirwayBillHistoryId(Long airwayBillHistoryId) {
		this.airwayBillHistoryId = airwayBillHistoryId;
	}

	public Long getActivityApprovalStatusId() {
		return this.activityApprovalStatusId;
	}

	public void setActivityApprovalStatusId(Long activityApprovalStatusId) {
		this.activityApprovalStatusId = activityApprovalStatusId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getAirwayBillPickupDateTime() {
		return this.airwayBillPickupDateTime;
	}

	public void setAirwayBillPickupDateTime(Timestamp airwayBillPickupDateTime) {
		this.airwayBillPickupDateTime = airwayBillPickupDateTime;
	}

	public Timestamp getAirwayBillTimestamp() {
		return this.airwayBillTimestamp;
	}

	public void setAirwayBillTimestamp(Timestamp airwayBillTimestamp) {
		this.airwayBillTimestamp = airwayBillTimestamp;
	}

	public String getAwbNumber() {
		return this.awbNumber;
	}

	public void setAwbNumber(String awbNumber) {
		this.awbNumber = awbNumber;
	}

	public Long getAwbStatusId() {
		return this.awbStatusId;
	}

	public void setAwbStatusId(Long awbStatusId) {
		this.awbStatusId = awbStatusId;
	}

	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateOfReturn() {
		return this.dateOfReturn;
	}

	public void setDateOfReturn(Date dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}

	public String getDeliveryOrder() {
		return this.deliveryOrder;
	}

	public void setDeliveryOrder(String deliveryOrder) {
		this.deliveryOrder = deliveryOrder;
	}

	public String getDestCode() {
		return this.destCode;
	}

	public void setDestCode(String destCode) {
		this.destCode = destCode;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Long getDistributionCartId() {
		return this.distributionCartId;
	}

	public void setDistributionCartId(Long distributionCartId) {
		this.distributionCartId = distributionCartId;
	}

	public String getGdnReference() {
		return this.gdnReference;
	}

	public void setGdnReference(String gdnReference) {
		this.gdnReference = gdnReference;
	}

	public BigDecimal getGiftWrapCharge() {
		return this.giftWrapCharge;
	}

	public void setGiftWrapCharge(BigDecimal giftWrapCharge) {
		this.giftWrapCharge = giftWrapCharge;
	}

	public Timestamp getHistoryTimestamp() {
		return this.historyTimestamp;
	}

	public void setHistoryTimestamp(Timestamp historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}

	public BigDecimal getInsuranceCharge() {
		return this.insuranceCharge;
	}

	public void setInsuranceCharge(BigDecimal insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}

	public Long getInvoiceApprovalStatusId() {
		return this.invoiceApprovalStatusId;
	}

	public void setInvoiceApprovalStatusId(Long invoiceApprovalStatusId) {
		this.invoiceApprovalStatusId = invoiceApprovalStatusId;
	}

	public Long getLogisticsProviderId() {
		return this.logisticsProviderId;
	}

	public void setLogisticsProviderId(Long logisticsProviderId) {
		this.logisticsProviderId = logisticsProviderId;
	}

	public String getNoteReturn() {
		return this.noteReturn;
	}

	public void setNoteReturn(String noteReturn) {
		this.noteReturn = noteReturn;
	}

	public String getNoteUndelivered() {
		return this.noteUndelivered;
	}

	public void setNoteUndelivered(String noteUndelivered) {
		this.noteUndelivered = noteUndelivered;
	}

	public Long getOrderItemId() {
		return this.orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public BigDecimal getOtherCharge() {
		return this.otherCharge;
	}

	public void setOtherCharge(BigDecimal otherCharge) {
		this.otherCharge = otherCharge;
	}

	public BigDecimal getPackageWeight() {
		return this.packageWeight;
	}

	public void setPackageWeight(BigDecimal packageWeight) {
		this.packageWeight = packageWeight;
	}

	public String getPieces() {
		return this.pieces;
	}

	public void setPieces(String pieces) {
		this.pieces = pieces;
	}

	public BigDecimal getPricePerKg() {
		return this.pricePerKg;
	}

	public void setPricePerKg(BigDecimal pricePerKg) {
		this.pricePerKg = pricePerKg;
	}

	public Date getReceived() {
		return this.received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getRecipient() {
		return this.recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getRelation() {
		return this.relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getReturn_() {
		return this.return_;
	}

	public void setReturn_(String return_) {
		this.return_ = return_;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getShipper() {
		return this.shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTariff() {
		return this.tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}

	public BigDecimal getTotalCharge() {
		return this.totalCharge;
	}

	public void setTotalCharge(BigDecimal totalCharge) {
		this.totalCharge = totalCharge;
	}

	public String getTrackingNumber() {
		return this.trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUndelivered() {
		return this.undelivered;
	}

	public void setUndelivered(Date undelivered) {
		this.undelivered = undelivered;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public VenMasterChangeType getVenMasterChangeType() {
		return this.venMasterChangeType;
	}

	public void setVenMasterChangeType(VenMasterChangeType venMasterChangeType) {
		this.venMasterChangeType = venMasterChangeType;
	}
	
}