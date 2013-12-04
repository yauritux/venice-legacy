package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the log_airway_bill_retur database table.
 * 
 */
@Entity
@Table(name="log_airway_bill_retur")
public class LogAirwayBillRetur implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_airway_bill_retur")  
	@TableGenerator(name="log_airway_bill_retur", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="airway_bill_retur_id")
	private Long airwayBillReturId;

	@Column(name="activity_approved_by_user_id")
	private String activityApprovedByUserId;

	@Column(name="activity_file_name_and_loc")
	private String activityFileNameAndLoc;

	@Column(name="activity_result_status")
	private String activityResultStatus;

    @Temporal( TemporalType.DATE)
	@Column(name="actual_pickup_date")
	private Date actualPickupDate;
    
	//bi-directional many-to-one association to LogApprovalStatus
    @ManyToOne
	@JoinColumn(name="invoice_approval_status_id")
	private LogApprovalStatus logApprovalStatus1;

	//bi-directional many-to-one association to LogApprovalStatus
    @ManyToOne
	@JoinColumn(name="activity_approval_status_id")
	private LogApprovalStatus logApprovalStatus2;

	private String address;

	@Column(name="airway_bill_number")
	private String airwayBillNumber;

	@Column(name="airway_bill_pickup_date_time")
	private Timestamp airwayBillPickupDateTime;

	@Column(name="airway_bill_timestamp")
	private Timestamp airwayBillTimestamp;

	private String consignee;

	@Column(name="contact_person")
	private String contactPerson;

	private String content;

	@Column(name="current_logistics_provider_id")
	private Long currentLogisticsProviderId;

    @Temporal( TemporalType.DATE)
	@Column(name="date_of_return")
	private Date dateOfReturn;

	@Column(name="delivery_order")
	private String deliveryOrder;

	@Column(name="dest_code")
	private String destCode;

	private String destination;

	@Column(name="distribution_cart_id")
	private Long distributionCartId;

	@Column(name="gdn_reference")
	private String gdnReference;

	@Column(name="gift_wrap_charge")
	private BigDecimal giftWrapCharge;

	@Column(name="insurance_charge")
	private BigDecimal insuranceCharge;

	@Column(name="insured_amount")
	private BigDecimal insuredAmount;

	@Column(name="invoice_airwaybill_record_id")
	private Long invoiceAirwaybillRecordId;

	@Column(name="invoice_approved_by_user_id")
	private String invoiceApprovedByUserId;

	@Column(name="invoice_file_name_and_loc")
	private String invoiceFileNameAndLoc;

	@Column(name="invoice_result_status")
	private String invoiceResultStatus;

	@Column(name="kpi_delivery_perf_clocked")
	private Boolean kpiDeliveryPerfClocked;

	@Column(name="kpi_invoice_accuracy_clocked")
	private Boolean kpiInvoiceAccuracyClocked;

	@Column(name="kpi_pickup_perf_clocked")
	private Boolean kpiPickupPerfClocked;

	@Column(name="mta_data")
	private Boolean mtaData;

	@Column(name="note_return")
	private String noteReturn;

	@Column(name="note_undelivered")
	private String noteUndelivered;

	@Column(name="num_packages")
	private Integer numPackages;

	private String origin;

	@Column(name="other_charge")
	private BigDecimal otherCharge;

	@Column(name="package_weight")
	private BigDecimal packageWeight;

	@Column(name="price_per_kg")
	private BigDecimal pricePerKg;

	@Column(name="provider_total_charge")
	private BigDecimal providerTotalCharge;

    @Temporal( TemporalType.DATE)
	private Date received;

	private String recipient;

	private String relation;

	@Column(name="return")
	private String return_;

	private Integer sequence;

	private String service;

	private String shipper;

	private String status;

	private String tariff;

	@Column(name="total_charge")
	private BigDecimal totalCharge;

	@Column(name="tracking_number")
	private String trackingNumber;

	private String type;

    @Temporal( TemporalType.DATE)
	private Date undelivered;

	private String zip;

	//bi-directional many-to-one association to VenReturItem
    @ManyToOne
	@JoinColumn(name="retur_item_id")
	private VenReturItem venReturItem;
    
	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

    public LogAirwayBillRetur() {
    }

	public Long getAirwayBillReturId() {
		return this.airwayBillReturId;
	}

	public void setAirwayBillReturId(Long airwayBillReturId) {
		this.airwayBillReturId = airwayBillReturId;
	}

	public String getActivityApprovedByUserId() {
		return this.activityApprovedByUserId;
	}

	public void setActivityApprovedByUserId(String activityApprovedByUserId) {
		this.activityApprovedByUserId = activityApprovedByUserId;
	}

	public String getActivityFileNameAndLoc() {
		return this.activityFileNameAndLoc;
	}

	public void setActivityFileNameAndLoc(String activityFileNameAndLoc) {
		this.activityFileNameAndLoc = activityFileNameAndLoc;
	}

	public String getActivityResultStatus() {
		return this.activityResultStatus;
	}

	public void setActivityResultStatus(String activityResultStatus) {
		this.activityResultStatus = activityResultStatus;
	}

	public Date getActualPickupDate() {
		return this.actualPickupDate;
	}

	public void setActualPickupDate(Date actualPickupDate) {
		this.actualPickupDate = actualPickupDate;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAirwayBillNumber() {
		return this.airwayBillNumber;
	}

	public void setAirwayBillNumber(String airwayBillNumber) {
		this.airwayBillNumber = airwayBillNumber;
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

	public Long getCurrentLogisticsProviderId() {
		return this.currentLogisticsProviderId;
	}

	public void setCurrentLogisticsProviderId(Long currentLogisticsProviderId) {
		this.currentLogisticsProviderId = currentLogisticsProviderId;
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

	public BigDecimal getInsuranceCharge() {
		return this.insuranceCharge;
	}

	public void setInsuranceCharge(BigDecimal insuranceCharge) {
		this.insuranceCharge = insuranceCharge;
	}

	public BigDecimal getInsuredAmount() {
		return this.insuredAmount;
	}

	public void setInsuredAmount(BigDecimal insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	public Long getInvoiceAirwaybillRecordId() {
		return this.invoiceAirwaybillRecordId;
	}

	public void setInvoiceAirwaybillRecordId(Long invoiceAirwaybillRecordId) {
		this.invoiceAirwaybillRecordId = invoiceAirwaybillRecordId;
	}

	public String getInvoiceApprovedByUserId() {
		return this.invoiceApprovedByUserId;
	}

	public void setInvoiceApprovedByUserId(String invoiceApprovedByUserId) {
		this.invoiceApprovedByUserId = invoiceApprovedByUserId;
	}

	public String getInvoiceFileNameAndLoc() {
		return this.invoiceFileNameAndLoc;
	}

	public void setInvoiceFileNameAndLoc(String invoiceFileNameAndLoc) {
		this.invoiceFileNameAndLoc = invoiceFileNameAndLoc;
	}

	public String getInvoiceResultStatus() {
		return this.invoiceResultStatus;
	}

	public void setInvoiceResultStatus(String invoiceResultStatus) {
		this.invoiceResultStatus = invoiceResultStatus;
	}

	public Boolean getKpiDeliveryPerfClocked() {
		return this.kpiDeliveryPerfClocked;
	}

	public void setKpiDeliveryPerfClocked(Boolean kpiDeliveryPerfClocked) {
		this.kpiDeliveryPerfClocked = kpiDeliveryPerfClocked;
	}

	public Boolean getKpiInvoiceAccuracyClocked() {
		return this.kpiInvoiceAccuracyClocked;
	}

	public void setKpiInvoiceAccuracyClocked(Boolean kpiInvoiceAccuracyClocked) {
		this.kpiInvoiceAccuracyClocked = kpiInvoiceAccuracyClocked;
	}

	public Boolean getKpiPickupPerfClocked() {
		return this.kpiPickupPerfClocked;
	}

	public void setKpiPickupPerfClocked(Boolean kpiPickupPerfClocked) {
		this.kpiPickupPerfClocked = kpiPickupPerfClocked;
	}

	public Boolean getMtaData() {
		return this.mtaData;
	}

	public void setMtaData(Boolean mtaData) {
		this.mtaData = mtaData;
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

	public Integer getNumPackages() {
		return this.numPackages;
	}

	public void setNumPackages(Integer numPackages) {
		this.numPackages = numPackages;
	}

	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
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

	public BigDecimal getPricePerKg() {
		return this.pricePerKg;
	}

	public void setPricePerKg(BigDecimal pricePerKg) {
		this.pricePerKg = pricePerKg;
	}

	public BigDecimal getProviderTotalCharge() {
		return this.providerTotalCharge;
	}

	public void setProviderTotalCharge(BigDecimal providerTotalCharge) {
		this.providerTotalCharge = providerTotalCharge;
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

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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

	public VenReturItem getVenReturItem() {
		return this.venReturItem;
	}

	public void setVenReturItem(VenReturItem venReturItem) {
		this.venReturItem = venReturItem;
	}

	public LogLogisticsProvider getLogLogisticsProvider() {
		return logLogisticsProvider;
	}

	public void setLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider) {
		this.logLogisticsProvider = logLogisticsProvider;
	}

	public LogApprovalStatus getLogApprovalStatus1() {
		return logApprovalStatus1;
	}

	public LogApprovalStatus getLogApprovalStatus2() {
		return logApprovalStatus2;
	}

	public void setLogApprovalStatus1(LogApprovalStatus logApprovalStatus1) {
		this.logApprovalStatus1 = logApprovalStatus1;
	}

	public void setLogApprovalStatus2(LogApprovalStatus logApprovalStatus2) {
		this.logApprovalStatus2 = logApprovalStatus2;
	}
	
}